//
//  FirstPage.swift
//  GetALiftDev
//
//  Created by Loan Aubergeon on 11/08/2017.
//  Copyright © 2017 Loan Aubergeon. All rights reserved.
//

import UIKit
import GoogleMaps
import NotificationBannerSwift

/// The first view of the app when you are authenticated
class FirstView: UIViewController, CLLocationManagerDelegate {
    
    //  #################### Variables ####################
    
    /// Initializes MapTasks
    var mapTasks = MapTasks()
    
    /// Initializes CLLLocationManager for current location
    let locationManager = CLLocationManager()
    
    /// The button to access the menu
    @IBOutlet weak var menuButton: UIBarButtonItem!
    
    /// The view of the Map
    @IBOutlet var viewMap : GMSMapView?
    
    /// Variable for the current location
    var didFindMyLocation = false
    
    /// The current location marker
    var locationMarker: GMSMarker!
    
    /// The user's token
    var token = Home.UserConnectedInformations.userToken
    
    
    //  #################### Functions ####################
    
    /// When the the view is load
    override func viewDidLoad() {
        super.viewDidLoad()
        
        /// Add the manu button on the view
        if self.revealViewController() != nil {
            menuButton.target = self.revealViewController()
            menuButton.action = #selector(SWRevealViewController.revealToggle(_:))
            self.view.addGestureRecognizer(self.revealViewController().panGestureRecognizer())
        }
        
        /// Initialize the Map view to a position of the University of Mata
        let camera: GMSCameraPosition = GMSCameraPosition.camera(withLatitude: 48.857165, longitude: 2.354613, zoom: 8.0)
        viewMap?.camera = camera
        
        /// Set up the location button on the view
        viewMap?.settings.myLocationButton = true
        
        /// Update the Map view with the current position of the user
        locationManager.delegate = self
        locationManager.requestWhenInUseAuthorization()
        locationManager.desiredAccuracy = kCLLocationAccuracyBest
        
        
        locationManager.startUpdatingLocation()
        
        
        
        SearchRoute.SearchedRoute.seeCurrentRoute = Route.init()
        

    }
    
    override func viewDidAppear(_ animated: Bool) {
        
    }
    
    /// For change the type of the map
    /// Create a AlertController to choose the type of the map
    /*@IBAction func changeMapType(sender: AnyObject) {
        
        let actionSheet = UIAlertController(title: "Map Types", message: "Select map type :", preferredStyle: UIAlertControllerStyle.actionSheet)
        
        /// Type Normal
        let normalMapTypeAction = UIAlertAction(title: "Normal", style: UIAlertActionStyle.default) { (alertAction) -> Void in
            self.viewMap?.mapType = kGMSTypeNormal
        }
        
        /// Type Terrain
        let terrainMapTypeAction = UIAlertAction(title: "Terrain", style: UIAlertActionStyle.default) { (alertAction) -> Void in
            self.viewMap?.mapType = kGMSTypeTerrain
        }
        
        /// Type Hybrid
        let hybridMapTypeAction = UIAlertAction(title: "Hybrid", style: UIAlertActionStyle.default) { (alertAction) -> Void in
            self.viewMap?.mapType = kGMSTypeHybrid
        }
        
        let cancelAction = UIAlertAction(title: "Close", style: UIAlertActionStyle.cancel) { (alertAction) -> Void in
            
        }
        
        actionSheet.addAction(normalMapTypeAction)
        actionSheet.addAction(terrainMapTypeAction)
        actionSheet.addAction(hybridMapTypeAction)
        actionSheet.addAction(cancelAction)
        
        present(actionSheet, animated: true, completion: nil)
    }*/
    
    /// Verify the location authorization
    @nonobjc func locationManager(manager: CLLocationManager!, didChangeAuthorizationStatus status: CLAuthorizationStatus) {
        if status == CLAuthorizationStatus.authorizedWhenInUse {
            locationManager.startUpdatingLocation()
            viewMap?.isMyLocationEnabled = true
            viewMap?.settings.myLocationButton = true
        }
    }
    
    /// For indicate the current location
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        let userLocation = locations.last
        let camera = GMSCameraPosition.camera(withLatitude: userLocation!.coordinate.latitude,
                                              longitude: userLocation!.coordinate.longitude, zoom: 13.0)
        viewMap?.isMyLocationEnabled = true
        viewMap?.camera = camera

        locationManager.stopUpdatingLocation()
    }
    
}
