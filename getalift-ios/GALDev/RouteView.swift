//
//  RouteView.swift
//  GALDev
//
//  Created by Loan Aubergeon on 11/09/2017.
//  Copyright © 2017 Loan Aubergeon. All rights reserved.
//

import Foundation
import GoogleMaps
import NotificationBannerSwift


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
    var rideTasks = RideTasks()
    var passengerTasks = PassengerTasks()
    
    
    /// User's Token
    var token = Home.UserConnectedInformations.userToken
    
    var user : User = Home.UserConnectedInformations.user
    
    var routes : [Route] = []
    
    /// Labels for displaying information
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
        
        // The right button to add a route to rides
        let addToRidesButtonItem = UIBarButtonItem.init(
            image: #imageLiteral(resourceName: "addToRides"),
            style: .done,
            target: self,
            action: #selector(addToRides(sender:))
        )
        
        self.navigationItem.rightBarButtonItems = [rightButtonItem, walkrightButtonItem, addToRidesButtonItem]
    }
    
    /// To diplay the driver view
    @IBAction func displayDriverView(sender: AnyObject){
        performSegue(withIdentifier: "driverViewSegue", sender: self)
    }
    
    //To send data from one page to another
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
    
    ///Add the route to the ride
    
    //id of ride which is created
    var insertId: Int = 0
    
    @IBAction func addToRides(sender: Any) {
        rideTasks.addRide(routeID: self.routes[myIndex].id, completitionHandler: { (status, success) -> Void in
            if success { //When there is no existing ride for the selected route and a new ride is created
                DispatchQueue.main.async() {
                    self.insertId = self.rideTasks.insertId
                    
                    //Creation of the passenger regarding the ride which was created before
                    self.passengerTasks.addpass(ride: self.insertId, passenger: self.user.id)
                    
                    let imageView = UIImageView(image: #imageLiteral(resourceName: "success"))
                    let banner = NotificationBanner(title: "Route added to your Rides", subtitle: "Driver must confirm this Ride", leftView: imageView, style: .success)
                    banner.show()
                }
            } else { //When a ride already exists for the selected route
                DispatchQueue.main.async {
                    self.passengerTasks.addPassengerExistingRide(passengerID: self.user.id, routeID: self.routes[myIndex].id, completitionHandler: { (status, success) in
                        DispatchQueue.main.async {
                            if success { //When the user is not yet a passenger for the ride
                                let imageView = UIImageView(image: #imageLiteral(resourceName: "success"))
                                let banner = NotificationBanner(title: "Route added to your Rides", subtitle: "Driver must confirm this Ride", leftView: imageView, style: .success)
                                banner.show()
                            } else { //When the user is already a passenger for the ride
                                let imageView = UIImageView(image: #imageLiteral(resourceName: "failed"))
                                let banner = NotificationBanner(title: "This route is already in your Ride", subtitle: "You already had this route to your Rides", leftView: imageView, style: .danger)
                                banner.show()
                            }
                        }
                    })
                }
            }
        })
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
        let closestPointStart = String(self.routes[myIndex].latitudeOfStartingPoint)+","+String(self.routes[myIndex].longitudeOfStartingPoint)
        let closestPointEnd = String(self.routes[myIndex].latitudeOfEndPoint)+","+String(self.routes[myIndex].longitudeOfEndPoint)
        let driverDestination = self.routes[myIndex].nameOfEndpoint
        let searchedOrigin = String(SearchRoute.SearchedRoute.searchedRoute.latitudeOfStartingPoint)+","+String(SearchRoute.SearchedRoute.searchedRoute.longitudeOfStartingPoint)
        let searchedDestination = String(SearchRoute.SearchedRoute.searchedRoute.latitudeOfEndPoint)+","+String(SearchRoute.SearchedRoute.searchedRoute.longitudeOfEndPoint)
        let routeId = self.routes[myIndex].id
        
        DispatchQueue.main.async() {
            self.originLabel?.text = self.routes[myIndex].nameOfStartingPoint
            self.destinationLabel?.text = self.routes[myIndex].nameOfEndpoint
        }
        
        // Request to find the driver's name
        self.userTasks.user(driverId: driverIndex, completionHandler: { (status, success) -> Void in
            if success {
                DispatchQueue.main.async() {
                    self.usernameDriverLabel?.text = self.userTasks.user.username
                }
            }
        })
        // Path traveled by the driver
        self.mapTasks.getDirections(origin: driverOrigin, destination: driverDestination, waypoints: nil, travelMode: nil, completionHandler: { (status, success) -> Void in
            if success {
                self.mapTasks.calculateTotalDistanceAndDuration()
                let nameOfStartingPoint = self.mapTasks.originAddress
                let latitudeOfStartingPoint = self.mapTasks.originCoordinate.latitude
                let longitudeOfStartingPoint = self.mapTasks.originCoordinate.longitude
                let nameOfEndpoint = self.mapTasks.destinationAddress
                let latitudeOfEndPoint = self.mapTasks.destinationCoordinate.latitude
                let longitudeOfEndPoint = self.mapTasks.destinationCoordinate.longitude
                let overviewPolyline = self.mapTasks.overviewPolyline
                let totalDistanceInMetter = self.mapTasks.totalDistanceInMeters
                let distance = self.mapTasks.totalDistance
                let duration = self.mapTasks.totalDuration

                self.driverRoute = Route.init(nameOfStartingPoint: nameOfStartingPoint!,
                                         latitudeOfStartingPoint: latitudeOfStartingPoint,
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
        
        // First path traveled by the passenger on foot
        self.mapTasks.getDirectionsWalking(origin: searchedOrigin, destination: closestPointStart, waypoints: nil, travelMode: nil, completionHandler: { (status, success) -> Void in
            if success {
                let nameOfStartingPoint = self.mapTasks.originAddress
                let latitudeOfStartingPoint = self.mapTasks.originCoordinate.latitude
                let longitudeOfStartingPoint = self.mapTasks.originCoordinate.longitude
                let nameOfEndpoint = self.mapTasks.destinationAddress
                let latitudeOfEndPoint = self.mapTasks.destinationCoordinate.latitude
                let longitudeOfEndPoint = self.mapTasks.destinationCoordinate.longitude
                let overviewPolyline = self.mapTasks.overviewPolyline
                let totalDistanceInMetter = self.mapTasks.totalDistanceInMeters
                
                self.mapTasks.calculateTotalDistanceAndDuration()
                
                self.routeOnFootOne = Route.init(nameOfStartingPoint: nameOfStartingPoint!,
                                              latitudeOfStartingPoint: latitudeOfStartingPoint,
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
        
        // Second path traveled by the passenger on foot
        self.mapTasks.getDirectionsWalking(origin: closestPointEnd, destination: searchedDestination, waypoints: nil, travelMode: nil, completionHandler: { (status, success) -> Void in
            if success {
                let nameOfStartingPoint = self.mapTasks.originAddress
                let latitudeOfStartingPoint = self.mapTasks.originCoordinate.latitude
                let longitudeOfStartingPoint = self.mapTasks.originCoordinate.longitude
                let nameOfEndpoint = self.mapTasks.destinationAddress
                let latitudeOfEndPoint = self.mapTasks.destinationCoordinate.latitude
                let longitudeOfEndPoint = self.mapTasks.destinationCoordinate.longitude
                let overviewPolyline = self.mapTasks.overviewPolyline
                let totalDistanceInMetter = self.mapTasks.totalDistanceInMeters
                
                self.mapTasks.calculateTotalDistanceAndDuration()
                
                self.routeOnFootTwo = Route.init(nameOfStartingPoint: nameOfStartingPoint!,
                                              latitudeOfStartingPoint: latitudeOfStartingPoint,
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
        // First Marker
        driverOriginMarker = GMSMarker(position: CLLocationCoordinate2DMake(self.driverRoute.latitudeOfStartingPoint as Double, self.driverRoute.longitudeOfStartingPoint as Double))
        driverOriginMarker.map = self.viewMap
        driverOriginMarker.icon = GMSMarker.markerImage(with: UIColor.init(red: 0.0471, green: 0.5686, blue: 0.0275, alpha: 1))
        driverOriginMarker.title = self.driverRoute.nameOfStartingPoint
        
        // Second Marker
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
        
        // Camera configuration
        // We recover the coordinates of the two points
        let oLat = self.driverRoute.latitudeOfStartingPoint
        let oLong = self.driverRoute.longitudeOfStartingPoint
        let dLat = self.driverRoute.latitudeOfEndPoint
        let dLong = self.driverRoute.longitudeOfEndPoint
        
        self.calculationForMapDisplay.centerCalcul(xA: oLat!, yA: oLong!, xB: dLat!, yB: dLong!)
        // We center the camera in relation to the two points
        // We apply the zoom according to the distance
        let totalDistance = (self.driverRoute.totalDistanceInMetter!)
        let zoom : Float = self.calculationForMapDisplay.zoomCalcul(distance: Double(totalDistance/1000))
        viewMap?.camera = GMSCameraPosition.camera(withLatitude: self.calculationForMapDisplay.xCenter, longitude: self.calculationForMapDisplay.yCenter, zoom: zoom)
        
    }
    
    
    func drawRoutesWithWalkPath(){
        self.viewMap?.clear()
        
        // First path
        
        // First Marker
        userOriginMarker = GMSMarker(position: CLLocationCoordinate2DMake(self.routeOnFootOne.latitudeOfStartingPoint as Double, self.routeOnFootOne.longitudeOfStartingPoint as Double))
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
        
        
        // Second path
        
        // First Marker
        driverOriginMarker = GMSMarker(position: CLLocationCoordinate2DMake(self.driverRoute.latitudeOfStartingPoint as Double, self.driverRoute.longitudeOfStartingPoint as Double))
        driverOriginMarker.map = self.viewMap
        driverOriginMarker.icon = GMSMarker.markerImage(with: UIColor.init(red: 0.0471, green: 0.5686, blue: 0.0275, alpha: 1))
        driverOriginMarker.title = self.driverRoute.nameOfStartingPoint
        
        // Second Marker
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
        
        // Third path
        
        let routeOneFootTwo = self.routeOnFootTwo.overviewPolyline["points"] as! String
        
        let pathTwo: GMSPath = GMSPath(fromEncodedPath: routeOneFootTwo)!
        routeTwoPolyline = GMSPolyline(path: pathTwo)
        routeTwoPolyline.strokeWidth = 4
        routeTwoPolyline.strokeColor = UIColor.init(red: 93/255, green: 146/255, blue: 253/255, alpha: 1)
        routeTwoPolyline.map = viewMap
        routeTwoPolyline.isTappable = true
        routeTwoPolyline.title = "On foot"
        
        // Second Marker
        userDestinationMarker = GMSMarker(position: CLLocationCoordinate2DMake(self.routeOnFootTwo.latitudeOfEndPoint as Double, self.routeOnFootTwo.longitudeOfEndPoint as Double))
        userDestinationMarker.map = self.viewMap
        userDestinationMarker.icon = GMSMarker.markerImage(with: UIColor.init(red: 243/255, green: 166/255, blue: 166/255, alpha: 1))
        userDestinationMarker.title = self.routeOnFootTwo.nameOfEndpoint
        
        // Camera configuration
        // We recover the coordinates of the two points
        let oLat = self.routeOnFootOne.latitudeOfStartingPoint
        let oLong = self.routeOnFootOne.longitudeOfStartingPoint
        let dLat = self.routeOnFootTwo.latitudeOfEndPoint
        let dLong = self.routeOnFootTwo.longitudeOfEndPoint
        
        self.calculationForMapDisplay.centerCalcul(xA: oLat!, yA: oLong!, xB: dLat!, yB: dLong!)
        // We center the camera in relation to the two points
        // We apply the zoom according to the distance
        
        let totalDistance = (self.driverRoute.totalDistanceInMetter! + self.routeOnFootOne.totalDistanceInMetter! + self.routeOnFootTwo.totalDistanceInMetter!)
        let zoom : Float = self.calculationForMapDisplay.zoomCalcul(distance: Double(totalDistance/1000))
        viewMap?.camera = GMSCameraPosition.camera(withLatitude: self.calculationForMapDisplay.xCenter, longitude: self.calculationForMapDisplay.yCenter, zoom: zoom)
        
        }
    
    
}
