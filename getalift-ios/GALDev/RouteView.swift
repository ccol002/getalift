//
//  RouteView.swift
//  GALDev
//
//  Created by Loan Aubergeon on 11/09/2017.
//  Copyright © 2017 Loan Aubergeon. All rights reserved.
//

import Foundation
import GoogleMaps


/// Class to display the information of an existing route
class RouteView : UIViewController {
    
    //  #################### Variables ####################
    
    /// Variables des Maps
    
    /// The map
    @IBOutlet var viewMap : GMSMapView?
    
    /// Markers
    var driverOriginMarker: GMSMarker!
    var userOriginMarker: GMSMarker!
    var driverDestinationMarker: GMSMarker!
    var userDestinationMarker: GMSMarker!
    
    /// The route (draw)
    var routeOnePolyline: GMSPolyline!
    var routeTwoPolyline: GMSPolyline!
    var routePolyline: GMSPolyline!
    
    /// Routes
    var driverRoute : Route = Route.init()
    var routeOnFootOne : Route = Route.init()
    var routeOnFootTwo : Route = Route.init()
    
    /// Tasks
    var mapTasks = MapTasks()
    var routeTasks = RouteTasks()
    var userTasks = UserTasks()
    var dateTasks = DateTasks()
    var calculationForMapDisplay = CalculationForMapDisplay()
    
    
    /// User's Token
    var token = Home.UserConnectedInformations.userToken
    
    var routes : [Route] = []
    
    /// Labels pour affichage des informations
    @IBOutlet var originLabel : UILabel?
    @IBOutlet var destinationLabel : UILabel?
    @IBOutlet var usernameDriverLabel : UILabel?
    @IBOutlet var dateLabel : UILabel?
    @IBOutlet var weeklyReccurence : UIImageView!
    @IBOutlet var durationLabel : UILabel?
    @IBOutlet var distanceLabel : UILabel?
    
    var searchedRoute : Route = Route.init()
    
    var compteur : Int = 0
    var compteurForWalkDisplay : Int = 0
    
    
     //  #################### Functions ####################
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.searchedRoute = SearchRoute.SearchedRoute.searchedRoute
        self.routeDisplay()
        self.compteur = 0
        
        /// The right button to show the driver controller
        let rightButtonItem = UIBarButtonItem.init(
            image: #imageLiteral(resourceName: "person"),
            style: .done,
            target: self,
            action: #selector(displayDriverView(sender:))
        )
        
        /// The right button to show the path on foot
        let walkrightButtonItem = UIBarButtonItem.init(
            image: #imageLiteral(resourceName: "walk2"),
            style: .done,
            target: self,
            action: #selector(displayWalkPath(sender:))
        )
        self.navigationItem.rightBarButtonItem = rightButtonItem
        self.navigationItem.rightBarButtonItems?.append(walkrightButtonItem)
    }
    
    /// To diplay the driver view
    @IBAction func displayDriverView(sender: AnyObject){
        performSegue(withIdentifier: "driverViewSegue", sender: self)
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "driverViewSegue" {
            if let destination = segue.destination as? DriverView {
                destination.routes = self.routes
            }
        }
    }
    
    /// Display the path on foot on the map
    @IBAction func displayWalkPath(sender: Any){
        self.compteurForWalkDisplay = self.compteurForWalkDisplay + 1
        
        if (self.compteurForWalkDisplay % 2 == 0){
            DispatchQueue.main.async() {
                self.drawDriverRoute()
                self.navigationItem.rightBarButtonItems![1].image = #imageLiteral(resourceName: "walk2")
            }
        } else {
            // On affiche la route avec le chemin à pied
            self.canDrawRoute()
            DispatchQueue.main.async() {
                self.navigationItem.rightBarButtonItems![1].image = #imageLiteral(resourceName: "car")
            }
        }
    }
    
    /// If tasks are finished, we can display the maps with his informations
    func canDrawRoute (){
        print(self.compteur)
        if self.compteur >= 3 {
            DispatchQueue.main.async() {
                self.drawRoutesWithWalkPath()
            }
        } else {
            self.routeDisplay()
        }
    }
    
    /// Display informations on a route
    func routeDisplay(){
        
        let driverIndex = self.routes[myIndex].driver
        let driverOrigin = self.routes[myIndex].nameOfStartingPoint
        let driverDestination = self.routes[myIndex].nameOfEndpoint
        let searchedOrigin = String(SearchRoute.SearchedRoute.searchedRoute.latitudeOfStartigPoint)+","+String(SearchRoute.SearchedRoute.searchedRoute.longitudeOfStartingPoint)
        let searchedDestination = String(SearchRoute.SearchedRoute.searchedRoute.latitudeOfEndPoint)+","+String(SearchRoute.SearchedRoute.searchedRoute.longitudeOfEndPoint)
        let routeId = self.routes[myIndex].id
        
        DispatchQueue.main.async() {
            self.originLabel?.text = self.routes[myIndex].nameOfStartingPoint
            self.destinationLabel?.text = self.routes[myIndex].nameOfEndpoint
        }
        
        // Requete pour trouver le nom du conducteur
        self.userTasks.user(driverId: driverIndex, completionHandler: { (status, success) -> Void in
            if success {
                DispatchQueue.main.async() {
                    self.usernameDriverLabel?.text = self.userTasks.user.username
                }
            }
        })
        // Chemin parcouru par le conducteur
        self.mapTasks.getDirections(origin: driverOrigin, destination: driverDestination, waypoints: nil, travelMode: nil, completionHandler: { (status, success) -> Void in
            if success {
                self.mapTasks.calculateTotalDistanceAndDuration()
                let nameOfStartingPoint = self.mapTasks.originAddress
                let latitudeOfStartigPoint = self.mapTasks.originCoordinate.latitude
                let longitudeOfStartingPoint = self.mapTasks.originCoordinate.longitude
                let nameOfEndpoint = self.mapTasks.destinationAddress
                let latitudeOfEndPoint = self.mapTasks.destinationCoordinate.latitude
                let longitudeOfEndPoint = self.mapTasks.destinationCoordinate.longitude
                let overviewPolyline = self.mapTasks.overviewPolyline
                let totalDistanceInMetter = self.mapTasks.totalDistanceInMeters
                let distance = self.mapTasks.totalDistance
                let duration = self.mapTasks.totalDuration

                self.driverRoute = Route.init(nameOfStartingPoint: nameOfStartingPoint!,
                                         latitudeOfStartigPoint: latitudeOfStartigPoint,
                                         longitudeOfStartingPoint: longitudeOfStartingPoint,
                                         nameOfEndpoint: nameOfEndpoint!,
                                         latitudeOfEndPoint: latitudeOfEndPoint,
                                         longitudeOfEndPoint: longitudeOfEndPoint,
                                         overviewPolyline: overviewPolyline,
                                         totalDistanceInMetter : Int(totalDistanceInMetter)
                )
                self.compteur = self.compteur + 1
                self.drawDriverRoute()
                DispatchQueue.main.async() {
                    self.durationLabel?.text = duration
                    self.distanceLabel?.text = distance
                }
                
            }
        })
        
        // Premier chemin parcouru par le passager à pied
        self.mapTasks.getDirectionsWalking(origin: searchedOrigin, destination: driverOrigin, waypoints: nil, travelMode: nil, completionHandler: { (status, success) -> Void in
            if success {
                let nameOfStartingPoint = self.mapTasks.originAddress
                let latitudeOfStartigPoint = self.mapTasks.originCoordinate.latitude
                let longitudeOfStartingPoint = self.mapTasks.originCoordinate.longitude
                let nameOfEndpoint = self.mapTasks.destinationAddress
                let latitudeOfEndPoint = self.mapTasks.destinationCoordinate.latitude
                let longitudeOfEndPoint = self.mapTasks.destinationCoordinate.longitude
                let overviewPolyline = self.mapTasks.overviewPolyline
                let totalDistanceInMetter = self.mapTasks.totalDistanceInMeters
                
                self.mapTasks.calculateTotalDistanceAndDuration()
                
                self.routeOnFootOne = Route.init(nameOfStartingPoint: nameOfStartingPoint!,
                                              latitudeOfStartigPoint: latitudeOfStartigPoint,
                                              longitudeOfStartingPoint: longitudeOfStartingPoint,
                                              nameOfEndpoint: nameOfEndpoint!,
                                              latitudeOfEndPoint: latitudeOfEndPoint,
                                              longitudeOfEndPoint: longitudeOfEndPoint,
                                              overviewPolyline: overviewPolyline,
                                              totalDistanceInMetter : Int(totalDistanceInMetter)
                )
                self.compteur = self.compteur + 1
            }
        })
        
        // Deuxieme chemin parcouru par le passager à pied
        self.mapTasks.getDirectionsWalking(origin: driverDestination, destination: searchedDestination, waypoints: nil, travelMode: nil, completionHandler: { (status, success) -> Void in
            if success {
                let nameOfStartingPoint = self.mapTasks.originAddress
                let latitudeOfStartigPoint = self.mapTasks.originCoordinate.latitude
                let longitudeOfStartingPoint = self.mapTasks.originCoordinate.longitude
                let nameOfEndpoint = self.mapTasks.destinationAddress
                let latitudeOfEndPoint = self.mapTasks.destinationCoordinate.latitude
                let longitudeOfEndPoint = self.mapTasks.destinationCoordinate.longitude
                let overviewPolyline = self.mapTasks.overviewPolyline
                let totalDistanceInMetter = self.mapTasks.totalDistanceInMeters
                
                self.mapTasks.calculateTotalDistanceAndDuration()
                
                self.routeOnFootTwo = Route.init(nameOfStartingPoint: nameOfStartingPoint!,
                                              latitudeOfStartigPoint: latitudeOfStartigPoint,
                                              longitudeOfStartingPoint: longitudeOfStartingPoint,
                                              nameOfEndpoint: nameOfEndpoint!,
                                              latitudeOfEndPoint: latitudeOfEndPoint,
                                              longitudeOfEndPoint: longitudeOfEndPoint,
                                              overviewPolyline: overviewPolyline,
                                              totalDistanceInMetter : Int(totalDistanceInMetter)
                )
               self.compteur = self.compteur + 1
            }

        })
        
        self.dateTasks.date(routeId: routeId, completionHandler: { (status, success) -> Void in
            if success {
                DispatchQueue.main.async() {
                    self.dateLabel?.text = self.dateTasks.date
                    self.dateLabel?.sizeToFit()
                    self.weeklyReccurence.isHidden = !self.dateTasks.weeklyReccurence
                }
            }
        })
    }

    func drawDriverRoute(){
        self.viewMap?.clear()
        // Premier Marker
        driverOriginMarker = GMSMarker(position: CLLocationCoordinate2DMake(self.driverRoute.latitudeOfStartigPoint as Double, self.driverRoute.longitudeOfStartingPoint as Double))
        driverOriginMarker.map = self.viewMap
        driverOriginMarker.icon = GMSMarker.markerImage(with: UIColor.init(red: 0.0471, green: 0.5686, blue: 0.0275, alpha: 1))
        driverOriginMarker.title = self.driverRoute.nameOfStartingPoint
        
        // Deuxieme Marker
        driverDestinationMarker = GMSMarker(position: CLLocationCoordinate2DMake(self.driverRoute.latitudeOfEndPoint as Double, self.driverRoute.longitudeOfEndPoint as Double))
        driverDestinationMarker.map = self.viewMap
        driverDestinationMarker.icon = GMSMarker.markerImage(with: UIColor.init(red: 0.6824, green: 0.0863, blue: 0.0863, alpha: 1))
        driverDestinationMarker.title = self.driverRoute.nameOfEndpoint
        
        // route
        let driverRoute = self.driverRoute.overviewPolyline["points"] as! String
        
        let pathDriver: GMSPath = GMSPath(fromEncodedPath: driverRoute)!
        routePolyline = GMSPolyline(path: pathDriver)
        routePolyline.strokeWidth = 5
        routePolyline.strokeColor = UIColor.init(red: 6/255, green: 57/255, blue: 159/255, alpha: 1)
        routePolyline.map = viewMap
        routePolyline.isTappable = true
        routePolyline.title = "Car ride"
        
        // Configuration de la camera
        // On recupere les coordonner des deux points
        let oLat = self.driverRoute.latitudeOfStartigPoint
        let oLong = self.driverRoute.longitudeOfStartingPoint
        let dLat = self.driverRoute.latitudeOfEndPoint
        let dLong = self.driverRoute.longitudeOfEndPoint
        
        self.calculationForMapDisplay.centerCalcul(xA: oLat!, yA: oLong!, xB: dLat!, yB: dLong!)
        // On centre la camera par rapport au deux points
        // On applique le zoom en fonction de la distance
        
        let totalDistance = (self.driverRoute.totalDistanceInMetter!)
        let zoom : Float = self.calculationForMapDisplay.zoomCalcul(distance: Double(totalDistance/1000))
        viewMap?.camera = GMSCameraPosition.camera(withLatitude: self.calculationForMapDisplay.xCenter, longitude: self.calculationForMapDisplay.yCenter, zoom: zoom)
        
    }
    
    
    func drawRoutesWithWalkPath(){
        self.viewMap?.clear()
        
        // Premier chemin
        
        // Premier Marker
        userOriginMarker = GMSMarker(position: CLLocationCoordinate2DMake(self.routeOnFootOne.latitudeOfStartigPoint as Double, self.routeOnFootOne.longitudeOfStartingPoint as Double))
        userOriginMarker.map = self.viewMap
        userOriginMarker.icon = GMSMarker.markerImage(with: UIColor.init(red: 146/255, green: 215/255, blue: 148/255, alpha: 1))
        userOriginMarker.title = self.routeOnFootOne.nameOfStartingPoint
        
        // route
        let routeOnFootOne = self.routeOnFootOne.overviewPolyline["points"] as! String
        
        let pathOne: GMSPath = GMSPath(fromEncodedPath: routeOnFootOne)!
        routeOnePolyline = GMSPolyline(path: pathOne)
        routeOnePolyline.strokeWidth = 4
        routeOnePolyline.strokeColor = UIColor.init(red: 93/255, green: 146/255, blue: 253/255, alpha: 1)
        routeOnePolyline.map = viewMap
        routeOnePolyline.isTappable = true
        routeOnePolyline.title = "On foot"
        
        
        // Deuxieme chemin
        
        // Premier Marker
        driverOriginMarker = GMSMarker(position: CLLocationCoordinate2DMake(self.driverRoute.latitudeOfStartigPoint as Double, self.driverRoute.longitudeOfStartingPoint as Double))
        driverOriginMarker.map = self.viewMap
        driverOriginMarker.icon = GMSMarker.markerImage(with: UIColor.init(red: 0.0471, green: 0.5686, blue: 0.0275, alpha: 1))
        driverOriginMarker.title = self.driverRoute.nameOfStartingPoint
        
        // Deuxieme Marker
        driverDestinationMarker = GMSMarker(position: CLLocationCoordinate2DMake(self.driverRoute.latitudeOfEndPoint as Double, self.driverRoute.longitudeOfEndPoint as Double))
        driverDestinationMarker.map = self.viewMap
        driverDestinationMarker.icon = GMSMarker.markerImage(with: UIColor.init(red: 0.6824, green: 0.0863, blue: 0.0863, alpha: 1))
        driverDestinationMarker.title = self.driverRoute.nameOfEndpoint
        
        // route
        let driverRoute = self.driverRoute.overviewPolyline["points"] as! String
        
        let pathDriver: GMSPath = GMSPath(fromEncodedPath: driverRoute)!
        routePolyline = GMSPolyline(path: pathDriver)
        routePolyline.strokeWidth = 5
        routePolyline.strokeColor = UIColor.init(red: 6/255, green: 57/255, blue: 159/255, alpha: 1)
        routePolyline.map = viewMap
        routePolyline.isTappable = true
        routePolyline.title = "Car ride"
        
        // Troisieme chemin
        
        let routeOneFootTwo = self.routeOnFootTwo.overviewPolyline["points"] as! String
        
        let pathTwo: GMSPath = GMSPath(fromEncodedPath: routeOneFootTwo)!
        routeTwoPolyline = GMSPolyline(path: pathTwo)
        routeTwoPolyline.strokeWidth = 4
        routeTwoPolyline.strokeColor = UIColor.init(red: 93/255, green: 146/255, blue: 253/255, alpha: 1)
        routeTwoPolyline.map = viewMap
        routeTwoPolyline.isTappable = true
        routeTwoPolyline.title = "On foot"
        
        // Deuxieme Marker
        userDestinationMarker = GMSMarker(position: CLLocationCoordinate2DMake(self.routeOnFootTwo.latitudeOfEndPoint as Double, self.routeOnFootTwo.longitudeOfEndPoint as Double))
        userDestinationMarker.map = self.viewMap
        userDestinationMarker.icon = GMSMarker.markerImage(with: UIColor.init(red: 243/255, green: 166/255, blue: 166/255, alpha: 1))
        userDestinationMarker.title = self.routeOnFootTwo.nameOfEndpoint
        
        // Configuration de la camera
        // On recupere les coordonner des deux points
        let oLat = self.routeOnFootOne.latitudeOfStartigPoint
        let oLong = self.routeOnFootOne.longitudeOfStartingPoint
        let dLat = self.routeOnFootTwo.latitudeOfEndPoint
        let dLong = self.routeOnFootTwo.longitudeOfEndPoint
        
        self.calculationForMapDisplay.centerCalcul(xA: oLat!, yA: oLong!, xB: dLat!, yB: dLong!)
        // On centre la camera par rapport au deux points
        // On applique le zoom en fonction de la distance
        
        let totalDistance = (self.driverRoute.totalDistanceInMetter! + self.routeOnFootOne.totalDistanceInMetter! + self.routeOnFootTwo.totalDistanceInMetter!)
        let zoom : Float = self.calculationForMapDisplay.zoomCalcul(distance: Double(totalDistance/1000))
        viewMap?.camera = GMSCameraPosition.camera(withLatitude: self.calculationForMapDisplay.xCenter, longitude: self.calculationForMapDisplay.yCenter, zoom: zoom)
        
        }
    
    
}
