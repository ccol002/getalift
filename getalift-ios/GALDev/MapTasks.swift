//
//  MapTasks.swift
//  FirstApp2
//
//  Created by Loan Aubergeon on 22/08/2017.
//  Copyright © 2017 Loan Aubergeon. All rights reserved.
//

import Foundation
import UIKit
import GoogleMaps

class MapTasks : NSObject, CLLocationManagerDelegate {
    
    // Adress
    
    let baseURLGeocode = "https://maps.googleapis.com/maps/api/geocode/json?"
    
    var lookupAddressResults: NSDictionary = [:]
    
    var fetchedFormattedAddress: String!
    
    var fetchedAddressLongitude: Double!
    
    var fetchedAddressLatitude: Double!
    
    
    // Direction
    
    let baseURLDirections = "https://maps.googleapis.com/maps/api/directions/json?"
    
    var selectedRoute: NSDictionary = [:]
    
    var overviewPolyline: NSDictionary = [:]
    
    var originCoordinate: CLLocationCoordinate2D!
    
    var destinationCoordinate: CLLocationCoordinate2D!
    
    var originAddress: String!
    
    var destinationAddress: String!
    
    
    // Distance
    
    var totalDistanceInMeters: UInt = 0
    
    var totalDistance: String!
    
    var totalDurationInSeconds: UInt = 0
    
    var totalDuration: String!
    
    
    
    func geocodeAddress(address: String!, withCompletionHandler completionHandler: @escaping ((_ status: String, _ success: Bool) -> Void)) {
        
        if let lookupAddress = address {
            
            let lookupAddress = lookupAddress.replacingOccurrences(of:" ", with: "+").replacingOccurrences(of: ",", with: "+")
            
            let geocodeURLString = baseURLGeocode + "address=" + lookupAddress
            
            let geocodeURL = NSURL(string: geocodeURLString)
            
            let requestAdress = NSMutableURLRequest(url:geocodeURL! as URL);
            
            requestAdress.httpMethod = "GET"
            
            let task = URLSession.shared.dataTask(with: requestAdress as URLRequest) {
                data, response, error in
                
                // Check for error
                if error != nil
                {
                    print("error=\(String(describing: error))")
                    return
                }
                
                do {
                    let dictionary: NSDictionary = try JSONSerialization.jsonObject(with: data! as Data, options: JSONSerialization.ReadingOptions.mutableContainers) as! NSDictionary
                    
                    
                    
                    
                    // Get the response status.
                    let status = dictionary["status"] as! String
                    
                    if status == "OK" {
                        let allResults: Array<NSDictionary>  = dictionary["results"] as! Array<NSDictionary>
                        self.lookupAddressResults = allResults[0] as NSDictionary
                        
                        // Keep the most important values.
                        self.fetchedFormattedAddress = self.lookupAddressResults["formatted_address"] as! String
                        let geometry = self.lookupAddressResults["geometry"] as! NSDictionary
                        self.fetchedAddressLongitude = ((geometry["location"] as! NSDictionary)["lng"] as! NSNumber).doubleValue
                        self.fetchedAddressLatitude = ((geometry["location"] as! NSDictionary)["lat"] as! NSNumber).doubleValue
                        completionHandler(status, true)
                        
                    }
                    else {
                        completionHandler("No valid address.", false)
                    }
                    
                    
                } catch let error as NSError {
                    print(error)
                    completionHandler("", false)
                }
                
                
            }
            task.resume()
        }
    }
    
    // Get direction
    
    func getDirections(origin: String!, destination: String!, waypoints: Array<String>!, travelMode: AnyObject!, completionHandler: @escaping ((_ status: String, _ success: Bool) -> Void)) {
        if let originLocation = origin {
            if let destinationLocation = destination {
                
                let originLocation = originLocation.replacingOccurrences(of:" ", with: "+").replacingOccurrences(of: ",", with: "+")
                let destinationLocation = destinationLocation.replacingOccurrences(of:" ", with: "+").replacingOccurrences(of: ",", with: "+")
                let directionsURLString = baseURLDirections + "origin=" + originLocation + "&destination=" + destinationLocation
                
                
            
                let directionsURL = URL(string: directionsURLString)!
                
                let requestDirection = NSMutableURLRequest(url:directionsURL as URL)
                
                requestDirection.httpMethod = "GET"
                
                let task = URLSession.shared.dataTask(with: requestDirection as URLRequest) {
                    data, response, error in
                    
                    // Check for error
                    if error != nil
                    {
                        print("error=\(String(describing: error))")
                        return
                    }
                    
                    do{
                        let dictionary: NSDictionary = try JSONSerialization.jsonObject(with: data! as Data, options: JSONSerialization.ReadingOptions.mutableContainers) as! NSDictionary
                        
                        let status = dictionary["status"] as! String
                        
                        if status == "OK" {
                            self.selectedRoute = (dictionary["routes"] as! Array<NSDictionary>)[0]
                            self.overviewPolyline = self.selectedRoute["overview_polyline"] as! NSDictionary
                            let legs = self.selectedRoute["legs"] as! Array<NSDictionary>
                            
                            let startLocationDictionary = legs[0]["start_location"] as! NSDictionary
                            self.originCoordinate = CLLocationCoordinate2DMake(startLocationDictionary["lat"] as! Double, startLocationDictionary["lng"] as! Double)
                            
                            let endLocationDictionary = legs[legs.count - 1]["end_location"] as! NSDictionary
                            self.destinationCoordinate = CLLocationCoordinate2DMake(endLocationDictionary["lat"] as! Double, endLocationDictionary["lng"] as! Double)
                            
                            self.originAddress = legs[0]["start_address"] as! String
                            self.destinationAddress = legs[legs.count - 1]["end_address"] as! String
                            
                            self.calculateTotalDistanceAndDuration()
                            
                            completionHandler(status, true)
                            
                        }
                        else {
                            completionHandler(status, false)
                        }
                        
                    } catch let error as NSError {
                        print(error)
                        completionHandler("", false)
                    }
                }
                task.resume()
            }
            else {
                completionHandler("Destination is nil.", false)
            }
        }
        else {
            completionHandler("Origin is nil", false)
        }
    }
    
    
    func getDirectionsWalking(origin: String!, destination: String!, waypoints: Array<String>!, travelMode: AnyObject!, completionHandler: @escaping ((_ status: String, _ success: Bool) -> Void)) {
        if let originLocation = origin {
            if let destinationLocation = destination {
                
                let originLocation = originLocation.replacingOccurrences(of:" ", with: "+").replacingOccurrences(of: ",", with: "+")
                let destinationLocation = destinationLocation.replacingOccurrences(of:" ", with: "+").replacingOccurrences(of: ",", with: "+")
                let directionsURLString = baseURLDirections + "origin=" + originLocation + "&destination=" + destinationLocation + "&mode=walking"
                
                let directionsURL = URL(string: directionsURLString)!
                
                let requestDirection = NSMutableURLRequest(url:directionsURL as URL)
                
                requestDirection.httpMethod = "GET"
                
                let task = URLSession.shared.dataTask(with: requestDirection as URLRequest) {
                    data, response, error in
                    
                    // Check for error
                    if error != nil
                    {
                        print("error=\(String(describing: error))")
                        return
                    }
                    
                    do{
                        let dictionary: NSDictionary = try JSONSerialization.jsonObject(with: data! as Data, options: JSONSerialization.ReadingOptions.mutableContainers) as! NSDictionary
                        
                        let status = dictionary["status"] as! String
                        
                        if status == "OK" {
                            self.selectedRoute = (dictionary["routes"] as! Array<NSDictionary>)[0]
                            self.overviewPolyline = self.selectedRoute["overview_polyline"] as! NSDictionary
                            let legs = self.selectedRoute["legs"] as! Array<NSDictionary>
                            
                            let startLocationDictionary = legs[0]["start_location"] as! NSDictionary
                            self.originCoordinate = CLLocationCoordinate2DMake(startLocationDictionary["lat"] as! Double, startLocationDictionary["lng"] as! Double)
                            
                            let endLocationDictionary = legs[legs.count - 1]["end_location"] as! NSDictionary
                            self.destinationCoordinate = CLLocationCoordinate2DMake(endLocationDictionary["lat"] as! Double, endLocationDictionary["lng"] as! Double)
                            
                            self.originAddress = legs[0]["start_address"] as! String
                            self.destinationAddress = legs[legs.count - 1]["end_address"] as! String
                            
                            self.calculateTotalDistanceAndDuration()
                            
                            completionHandler(status, true)
                            
                        }
                        else {
                            completionHandler(status, false)
                        }
                        
                    } catch let error as NSError {
                        print(error)
                        completionHandler("", false)
                    }
                }
                task.resume()
            }
            else {
                completionHandler("Destination is nil.", false)
            }
        }
        else {
            completionHandler("Origin is nil", false)
        }
    }
    
    
    func calculateTotalDistanceAndDuration() {
        let legs = self.selectedRoute["legs"] as! Array<NSDictionary>
        
        totalDistanceInMeters = 0
        totalDurationInSeconds = 0
        
        for leg in legs {
            totalDistanceInMeters += (leg["distance"] as! NSDictionary)["value"] as! UInt
            totalDurationInSeconds += (leg["duration"] as! NSDictionary)["value"] as! UInt
        }
        
        
        let distanceInKilometers: Double = Double(totalDistanceInMeters / 1000)
        totalDistance = "\(distanceInKilometers) Km"
        
        
        let mins = totalDurationInSeconds / 60
        let hours = mins / 60
        
        let remainingHours = hours
        let remainingMins = mins % 60
        
        totalDuration = "\(remainingHours)h\(remainingMins)"
    }
    
    
    
}
