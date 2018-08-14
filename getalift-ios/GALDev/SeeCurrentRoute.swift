//
//  SeeCurrentRoute.swift
//  GALDev
//
//  Created by administrator on 26/10/2017.
//  Copyright © 2017 Loan Aubergeon. All rights reserved.
//

import Foundation
import GoogleMaps

/// Class to show the route being edited on research interface
class SeeCurrentRoute : UIViewController{
    
    
    //  #################### Variables ####################

    /// The map
    @IBOutlet fileprivate weak var viewMap: GMSMapView!
    
    /// Markers
    var originMarker: GMSMarker!
    var destinationMarker: GMSMarker!
    
    /// The route (draw)
    var routePolyline: GMSPolyline!
    
    /// The searched route
    var currentRoute : Route = Route.init()
    
    var mapTasks = MapTasks()
    var calculationForMapDisplay = CalculationForMapDisplay()
    
    
     //  #################### Functions ####################
    
    override func viewDidLoad() {
        self.viewMap?.clear()
        viewMap.delegate = self
        self.currentRoute = SearchRoute.SearchedRoute.seeCurrentRoute
        self.routeDisplay()
    }
    
    func routeDisplay(){
        
        let origin = self.currentRoute.nameOfStartingPoint
        let destination = self.currentRoute.nameOfEndpoint
        
        if origin != nil && destination != nil {
            self.mapTasks.getDirections(origin: origin, destination: destination, waypoints: nil, travelMode: nil, completionHandler: { (status, success) -> Void in
                if success {
                    DispatchQueue.main.async() {
                        self.viewMap?.clear()
                        self.configureMapAndMarkersForRoute()
                        self.drawRoute()
                    }
                }
            })
        }
    }
    
    func drawRoute() {
        
        let route = mapTasks.overviewPolyline["points"] as! String
        
        let path: GMSPath = GMSPath(fromEncodedPath: route)!
        routePolyline = GMSPolyline(path: path)
        routePolyline.strokeWidth = 5
        routePolyline.strokeColor = UIColor.init(red: 6/255, green: 57/255, blue: 159/255, alpha: 1)
        routePolyline.map = viewMap
    }
    
    func configureMapAndMarkersForRoute() {
        
        // On recupere les coordonner des deux points
        let oLat = mapTasks.originCoordinate.latitude
        let oLong = mapTasks.originCoordinate.longitude
        let dLat = mapTasks.destinationCoordinate.latitude
        let dLong = mapTasks.destinationCoordinate.longitude
        
        self.calculationForMapDisplay.centerCalcul(xA: oLat, yA: oLong, xB: dLat, yB: dLong)
        // On centre la camera par rapport au deux points
        // On applique le zoom en fonction de la distance
        let zoom : Float = self.calculationForMapDisplay.zoomCalcul(distance: Double(self.mapTasks.totalDistanceInMeters/1000))
        
        viewMap?.camera = GMSCameraPosition.camera(withLatitude: self.calculationForMapDisplay.xCenter, longitude: self.calculationForMapDisplay.yCenter, zoom: zoom)
        
        originMarker = GMSMarker(position: self.mapTasks.originCoordinate)
        originMarker.map = self.viewMap
        originMarker.icon = GMSMarker.markerImage(with: UIColor.green)
        originMarker.title = self.mapTasks.originAddress
        originMarker.isDraggable = true
        
        destinationMarker = GMSMarker(position: self.mapTasks.destinationCoordinate)
        destinationMarker.map = self.viewMap
        destinationMarker.icon = GMSMarker.markerImage(with: UIColor.red)
        destinationMarker.title = self.mapTasks.destinationAddress
        destinationMarker.isDraggable = true
    }
   
}

extension SeeCurrentRoute: GMSMapViewDelegate{
    func mapView(_ mapView: GMSMapView, didEndDragging marker: GMSMarker) {
        
        

        let origin = String(self.originMarker.position.latitude)+","+String(self.originMarker.position.longitude)
        let destination = String(self.destinationMarker.position.latitude)+","+String(self.destinationMarker.position.longitude)
        self.mapTasks.getDirections(origin: origin, destination: destination, waypoints: nil, travelMode: nil, completionHandler: { (status, success) -> Void in
            if success {
                DispatchQueue.main.async() {
                    self.viewMap?.clear()
                    self.configureMapAndMarkersForRoute()
                    self.drawRoute()
                    SearchRoute.SearchedRoute.seeCurrentRoute.nameOfStartingPoint = String(self.originMarker.position.latitude)+","+String(self.originMarker.position.longitude)
                    SearchRoute.SearchedRoute.seeCurrentRoute.nameOfEndpoint = String(self.destinationMarker.position.latitude)+","+String(self.destinationMarker.position.longitude)
                }
            }
        })
        
    }
}


