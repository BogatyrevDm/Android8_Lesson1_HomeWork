package com.example.android8_lesson1_homework.view

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.example.android8_lesson1_homework.R
import com.example.android8_lesson1_homework.databinding.FragmentMapsBinding
import com.example.android8_lesson1_homework.utils.showSnackBar
import com.example.android8_lesson1_homework.viewmodel.AppState
import com.example.android8_lesson1_homework.viewmodel.MainViewModel

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import org.koin.android.viewmodel.ext.android.viewModel

private const val REQUEST_CODE = 12345
private const val REFRESH_PERIOD = 60000L
private const val MINIMAL_DISTANCE = 100f

class MapsFragment : Fragment() {
    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!

    private lateinit var map: GoogleMap

    private val mainViewModel: MainViewModel by viewModel()

    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap

        googleMap.setOnMapLongClickListener { latLng ->
            addMarkerToList(latLng)
        }
        googleMap.setOnMarkerClickListener() {
            onMarkerClick(it)
        }
        activateMyLocation(googleMap)
        getLocation()
        initViewModel()
    }

    private fun onMarkerClick(marker: Marker): Boolean {
        activity?.let {
            val bottomSheetDialogFragment = BottomSheetDialogFragment()

            bottomSheetDialogFragment.setData(marker.position.latitude, marker.position.longitude)
            bottomSheetDialogFragment.show(
                childFragmentManager, BottomSheetDialogFragment.TAG
            )
        }

        return false
    }

    private fun addMarkerToList(location: LatLng) {
        setMarker(location = location, resourceId = R.drawable.ic_map_pin)
        mainViewModel.setMarker(
            latitude = location.latitude,
            longitude = location.longitude
        )
    }

    private fun setMarker(location: LatLng, searchText: String? = null, resourceId: Int) {
        map.addMarker(
            MarkerOptions()
                .position(location)
                .title(searchText ?: "")
                .icon(BitmapDescriptorFactory.fromResource(resourceId))
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        checkPermission()
        mapFragment?.getMapAsync(callback)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_screen_menu, menu)

        super.onCreateOptionsMenu(menu, inflater)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_markers -> {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.map, MarkersFragment.newInstance())
                    .addToBackStack("")
                    .commitAllowingStateLoss()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
    fun initViewModel() {
        mainViewModel.getLiveData().observe(viewLifecycleOwner, Observer { renderData(it) })
        mainViewModel.getAllMarkers()
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {

                binding.map.visibility = View.VISIBLE
                binding.includedLoadingLayout.loadingLayout.visibility = View.GONE

                val markers = appState.markers
                for (marker in markers) {
                    val location = LatLng(marker.latitude, marker.longitude)
                    setMarker(location, marker.name, R.drawable.ic_map_pin)
                }
            }
            is AppState.Loading -> {
                binding.map.visibility = View.GONE
                binding.includedLoadingLayout.loadingLayout.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                binding.map.visibility = View.VISIBLE
                binding.includedLoadingLayout.loadingLayout.visibility = View.GONE
                binding.map.showSnackBar(
                    getString(R.string.error),
                    getString(R.string.reload),
                    {
                        mainViewModel.getAllMarkers()
                    })
            }
        }
    }

    private fun checkPermission() {
        activity?.let {
            when {
                ContextCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED -> {
                    getLocation()
                }
                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {

                    showRationaleDialog()
                }
                else -> {
                    requestPermission()
                }
            }
        }
    }

    private val onLocationListener = object : LocationListener {

        override fun onLocationChanged(location: Location) {
            context?.let {
                val coordinates = LatLng(
                    location.latitude,
                    location.longitude
                )
                map.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        coordinates,
                        15f
                    )
                )
            }
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    private fun activateMyLocation(googleMap: GoogleMap) {
        context?.let {
            val isPermissionGranted =
                ContextCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED
            googleMap.isMyLocationEnabled = isPermissionGranted
            googleMap.uiSettings.isMyLocationButtonEnabled = isPermissionGranted

        }
    }

    private fun getLocation() {
        activity?.let { context ->

            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                val locationManager =
                    context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    val provider = locationManager.getProvider(LocationManager.GPS_PROVIDER)
                    provider?.let {
                        locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            REFRESH_PERIOD,
                            MINIMAL_DISTANCE,
                            onLocationListener
                        )
                    }
                } else {
                    val location =
                        locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    if (location == null) {
                        showDialog(
                            getString(R.string.dialog_title_gps_turned_off),
                            getString(R.string.dialog_message_last_location_unknown)
                        )
                    } else {
                        showDialog(
                            getString(R.string.dialog_title_gps_turned_off),
                            getString(R.string.dialog_message_last_known_location)
                        )
                    }
                }
            } else {
                showRationaleDialog()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        checkPermissionsResult(requestCode, grantResults)
    }

    private fun requestPermission() {
        requestPermissions(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE

        )
    }

    private fun checkPermissionsResult(requestCode: Int, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CODE -> {
                var grantedPermissions = 0;
                if (grantResults.isNotEmpty()) {
                    for (i in grantResults) {
                        if (i == PackageManager.PERMISSION_GRANTED) {
                            grantedPermissions++
                        }
                    }
                    if (grantResults.size == grantedPermissions) {
                        getLocation()
                    } else {
                        showDialog(
                            getString(R.string.dialog_title_no_gps),
                            getString(R.string.dialog_message_no_gps)
                        )
                    }


                } else {
                    showDialog(
                        getString(R.string.dialog_title_no_gps),
                        getString(R.string.dialog_message_no_gps)
                    )
                }

            }
        }

    }

    private fun showDialog(title: String, message: String) {
        activity?.let {
            AlertDialog.Builder(it).setTitle(title).setMessage(message)
                .setNegativeButton("Закрыть") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
        }
    }

    private fun showRationaleDialog() {
        activity?.let {
            AlertDialog.Builder(it).setTitle(R.string.dialog_rationale_title).setMessage(R.string.dialog_rationale_meaasge)
                .setPositiveButton(R.string.dialog_rationale_give_access) { _, _ ->
                    requestPermission()
                }.setNegativeButton(R.string.dialog_rationale_decline) { dialog, _ ->
                    dialog.dismiss()
                }.create().show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = MapsFragment()
    }
}