//
//  CreateYourRoute.swift
//  GALDev
//
//  Created by Loan Aubergeon on 29/08/2017.
//  Copyright © 2017 Loan Aubergeon. All rights reserved.
//

import UIKit
import GoogleMaps
import NotificationBannerSwift

class CreateYourRoute : UIViewController, CLLocationManagerDelegate {
    
    
    //  #################### Variables ####################

    // Variables transmises par la requetes
    var user : User = Home.UserConnectedInformations.user
    var token = Home.UserConnectedInformations.userToken
    
    /// Route's informations 
    var origin : String! = SearchRoute.SearchedRoute.searchedRoute.nameOfStartingPoint
    var latitudeOfOrigin : Double! = SearchRoute.SearchedRoute.searchedRoute.latitudeOfStartigPoint
    var longitudeOfOrigin : Double! = SearchRoute.SearchedRoute.searchedRoute.longitudeOfStartingPoint
    var destination : String! = SearchRoute.SearchedRoute.searchedRoute.nameOfEndpoint
    var latitudeOfDestination : Double! = SearchRoute.SearchedRoute.searchedRoute.latitudeOfEndPoint
    var longitudeOfDestination : Double! = SearchRoute.SearchedRoute.searchedRoute.longitudeOfEndPoint
    var overviewPolyline : NSDictionary! = SearchRoute.SearchedRoute.searchedRoute.overviewPolyline
    var time : String! = SearchRoute.SearchedRoute.searchedRoute.time
    var date : String! = SearchRoute.SearchedRoute.searchedRoute.date
    var reccurence : Bool = SearchRoute.SearchedRoute.searchedRoute.recurrence
    var distance : String! = SearchRoute.SearchedRoute.searchedRoute.distance
    var duration : String! = SearchRoute.SearchedRoute.searchedRoute.duration
    
    
    // Variables utilisés pour afficher la map
    @IBOutlet var viewMap : GMSMapView?
    
    var originMarker: GMSMarker!
    
    var destinationMarker: GMSMarker!
    
    var routePolyline: GMSPolyline!
    
    var mapTasks = MapTasks()
    
    var calculationForMapDisplay = CalculationForMapDisplay()
    
    // Variables pour l'affichage des données
    
    @IBOutlet var originLabel : UILabel?
    @IBOutlet var destinationLabel : UILabel?
    @IBOutlet var timeLabel : UILabel?
    @IBOutlet var wReccurence : UIImageView!
    @IBOutlet var durationLabel : UILabel?
    @IBOutlet var distanceLabel : UILabel?
    
    
    // Variable pour ajouter la route dans la base de donnée
    
    @IBOutlet var button : UIButton?
    
    
     //  #################### Functions ####################
    
    override func viewDidLoad() {
        // Display
        timeLabel?.text = date+" "+time
        self.wReccurence.isHidden = !reccurence
        
        
        
        createRoute()
        
    }
    
    @IBAction func routeOnDataBase() {
        
        let reccurenceString : String = (self.reccurence ? "1" : "0")
        
        let driverId = user.id as! Int
        
        let url = NSURL(string: ServerAdress+":3000/api/routes")!
        
        var request = URLRequest(url: url as URL)
        
        request.httpMethod = "PUT"
        
        request.setValue("application/x-www-form-urlencoded", forHTTPHeaderField: "Content-Type")
        request.addValue(token, forHTTPHeaderField: "x-access-token")
        
        let postString = "startLat="+String(self.latitudeOfOrigin)+"&endLat="+String(self.latitudeOfDestination)+"&startLng="+String(self.longitudeOfOrigin)+"&endLng="+String(self.longitudeOfDestination)+"&origin="+self.origin+"&destination="+self.destination+"&distance="+self.distance+"&duration="+self.duration+"&driverId="+String(driverId)+"&dates="+self.date+" "+self.time+";"+reccurenceString
        
        request.httpBody = postString.data(using: .utf8)
        let viewController = self.storyboard?.instantiateViewController(withIdentifier: "transitionPage")
        self.present(viewController!, animated: true, completion: { () -> Void in
            DispatchQueue.main.async() {
                let imageView = UIImageView(image: #imageLiteral(resourceName: "success"))
                let banner = NotificationBanner(title: "Route added", subtitle: "The route has been added", leftView: imageView, style: .success)
                banner.show()
            }
        })
        
        
        
        do {
            // Execute the request
            let task = URLSession.shared.dataTask(with: request as URLRequest) {
                data, response, error in
                
                // Check for error
                if error != nil
                {
                    print("Error")
                    return
                }
            }
            task.resume()
        }
        
    }
    
    
    func createRoute() {
        self.viewMap?.clear()
        self.configureMapAndMarkersForRoute()
        self.drawRoute()
        self.displayRouteInfo()
    }
    
    
    
    func drawRoute() {
        let route = self.overviewPolyline["points"] as! String
        
        let path: GMSPath = GMSPath(fromEncodedPath: route)!
        routePolyline = GMSPolyline(path: path)
        routePolyline.strokeWidth = 5
        routePolyline.strokeColor = UIColor.init(red: 6/255, green: 57/255, blue: 159/255, alpha: 1)
        routePolyline.map = viewMap
    }
    
    func configureMapAndMarkersForRoute() {
        
        self.calculationForMapDisplay.centerCalcul(xA: latitudeOfOrigin, yA: longitudeOfOrigin, xB: latitudeOfDestination, yB: longitudeOfDestination)
        // On centre la camera par rapport au deux points
        // On applique le zoom en fonction de la distance
        let distanceDouble = Double(String(self.distance.characters.prefix(distance.count-2).dropLast()))
        let zoom : Float = self.calculationForMapDisplay.zoomCalcul(distance: distanceDouble!)
        
        viewMap?.camera = GMSCameraPosition.camera(withLatitude: self.calculationForMapDisplay.xCenter, longitude: self.calculationForMapDisplay.yCenter, zoom: zoom)
        
        
        // Mise en place des marqueurs
        let originCoordinate = CLLocationCoordinate2DMake(self.latitudeOfOrigin,self.longitudeOfOrigin)
        originMarker = GMSMarker(position: originCoordinate)
        originMarker.map = self.viewMap
        originMarker.icon = GMSMarker.markerImage(with: UIColor.green)
        originMarker.title = self.origin
        
        let destinationCoordinate = CLLocationCoordinate2DMake(self.latitudeOfDestination,self.longitudeOfDestination)
        destinationMarker = GMSMarker(position: destinationCoordinate)
        destinationMarker.map = self.viewMap
        destinationMarker.icon = GMSMarker.markerImage(with: UIColor.red)
        destinationMarker.title = self.destination
        
        // Ajout des adresses dans la barre du haut
        DispatchQueue.main.async(execute: {
            self.originLabel?.text = self.origin
            self.destinationLabel?.text = self.destination
            
        })
    }
    
    func displayRouteInfo() {
        DispatchQueue.main.async() {
            self.durationLabel?.text = self.duration
            self.distanceLabel?.text = self.distance
        }
    }
    
    
}

