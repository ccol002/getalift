//
//  SearchRoute.swift
//  GALDev
//
//  Created by Loan Aubergeon on 24/08/2017.
//  Copyright © 2017 Loan Aubergeon. All rights reserved.
//

import UIKit
import GoogleMaps

/// The view to search a route or create a route
class SearchRoute: UIViewController, CLLocationManagerDelegate {
    
    //  #################### Variables ####################
    
    /// Variables for the current location
    let locationManager = CLLocationManager()
    
    var locationMarker: GMSMarker!
    
    /// Currrent location cordinates
    var myLocationLat : Float = 0
    var myLocationLng : Float = 0
    
    /// The Maps who are displayed on the view
    @IBOutlet var viewMap : GMSMapView?
    
    /// TexteFields to retrieve Route's informations
    @IBOutlet var hourTextField: UITextField!
    @IBOutlet var dateTextField: UITextField!
    @IBOutlet var originTextField : SearchTextField!
    @IBOutlet var destinationTextField : SearchTextField!
    
    /// The switch for the weekly recurrence, The default value is False
    var affichageJour = false
    
    /// Tasks to retrieve informations with GoogleMaps
    var mapTasks = MapTasks()
    
    //  #################### Structures ####################
    
    /// To save informations about the searched route
    struct SearchedRoute {
        static var searchedRoute : Route = Route.init()
        static var seeCurrentRoute : Route = Route.init()
    }
    
    
    //  #################### Functions ####################
    
    override func viewDidLoad() {
        /// The information of the route sought is reset
        SearchedRoute.searchedRoute = Route.init()
        
        /// We have city suggestions on textfields
        citySuggestion()
        
        /// We start the location of the user's current location
        locationManager.delegate = self
        locationManager.requestWhenInUseAuthorization()
        locationManager.desiredAccuracy = kCLLocationAccuracyBest
        locationManager.startUpdatingLocation()
        
        NotificationCenter.default.addObserver(self, selector: #selector(self.keyboardWillShow), name: NSNotification.Name.UIKeyboardWillShow, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(self.keyboardWillHide), name: NSNotification.Name.UIKeyboardWillHide, object: nil)
        
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        NotificationCenter.default.removeObserver(self, name: NSNotification.Name.UIKeyboardWillShow, object: self.view.window)
        NotificationCenter.default.removeObserver(self, name: NSNotification.Name.UIKeyboardWillHide, object: self.view.window)
    }

    override func viewDidAppear(_ animated: Bool) {
        originTextField.text = SearchedRoute.seeCurrentRoute.nameOfStartingPoint
        destinationTextField.text = SearchedRoute.seeCurrentRoute.nameOfEndpoint
    }
    
    /// The list of city who are suggest on textfield
    func citySuggestion() {
        originTextField.theme = SearchTextFieldTheme.darkTheme()
        destinationTextField.theme = SearchTextFieldTheme.darkTheme()
        originTextField.filterStrings(["Attard","Balzan","Birgu","Birkirkara","Birzebbuga","Bormla","Dingli","Fgura","Fontana","Ghajnsielem","Gharb","Gharghur","Ghasri","Ghaxaq","Gudja","Gzira","Hamrun","Iklin","Imdina","Imgarr","Imqabba","Imsida","Imtarfa","Isla","Kalkara","Kercem","Kirkop","Lija","Luqa","Marsa","Marsaskala","Mellieha","Mosta","Munxar","Nadur","Naxxar","Paola","Pembroke","Pieta","Qala","Qormi","Qrendi","Rabat","Rabat","Safi","San Gwann","San Giljan","San Lawrenz","Saint Lucia","Saint Pauls Bay","Saint Venera","Sannat","Siggiewi","Sliema","Swieqi","Tarxien","Ta Xbiex","Valletta","Xaghra","Xewkija","Xghajra","Zabbar","Zebbug","Zebbug","Zejtun","Zurrieq"])
        destinationTextField.filterStrings(["Attard","Balzan","Birgu","Birkirkara","Birzebbuga","Bormla","Dingli","Fgura","Fontana","Ghajnsielem","Gharb","Gharghur","Ghasri","Ghaxaq","Gudja","Gzira","Hamrun","Iklin","Imdina","Imgarr","Imqabba","Imsida","Imtarfa","Isla","Kalkara","Kercem","Kirkop","Lija","Luqa","Marsa","Marsaskala","Mellieha","Mosta","Munxar","Nadur","Naxxar","Paola","Pembroke","Pieta","Qala","Qormi","Qrendi","Rabat","Rabat","Safi","San Gwann","San Giljan","San Lawrenz","Saint Lucia","Saint Pauls Bay","Saint Venera","Sannat","Siggiewi","Sliema","Swieqi","Tarxien","Ta Xbiex","Valletta","Xaghra","Xewkija","Xghajra","Zabbar","Zebbug","Zebbug","Zejtun","Zurrieq"])
    }
    
    override func awakeFromNib() {
        self.view.layoutIfNeeded()
    }
    
    @IBAction func myLocationOrigin(sender: UIButton){
        originTextField.text = String(myLocationLat)+" , "+String(myLocationLng)
    }
    
    @IBAction func myLocationDestination(sender: UIButton){
        destinationTextField.text = String(myLocationLat)+" , "+String(myLocationLng)
    }
    
    @IBAction func seeCurrentRoute(sender: UIButton){
        let origin = originTextField.text!
        let destination = destinationTextField.text!
        SearchedRoute.seeCurrentRoute = Route.init(origin: origin, destination: destination)
        self.performSegue(withIdentifier: "seeCurrentRoute", sender: nil)
    }
    
    @IBAction func go(sender: UIButton){
        
        let origin = originTextField.text!
        let destination = destinationTextField.text!
        let time = hourTextField.text!
        let date = dateTextField.text!
        let recurrence : Bool = affichageJour ? true : false
        
        
        self.mapTasks.getDirections(origin: origin, destination: destination, waypoints: nil, travelMode: nil, completionHandler: { (status, success) -> Void in
            if success {
                
                // Pour ne pas supprimer les données du texteview quand on revient sur la page de recherche
                let origin = self.originTextField.text!
                let destination = self.destinationTextField.text!
                SearchedRoute.seeCurrentRoute = Route.init(origin: origin, destination: destination)
                
                // On enregistre les données de recherche
                let originAdress = self.mapTasks.originAddress!
                let destinationAdress = self.mapTasks.destinationAddress!
                let latitudeOfOrigin = self.mapTasks.originCoordinate.latitude
                let longitudeOfOrigin = self.mapTasks.originCoordinate.longitude
                let latitudeOfDestination = self.mapTasks.destinationCoordinate.latitude
                let longititudeOfDestination = self.mapTasks.destinationCoordinate.longitude
                self.mapTasks.calculateTotalDistanceAndDuration()
                let distance = self.mapTasks.totalDistance!
                let duration = self.mapTasks.totalDuration!
                let overviewPolyline = self.mapTasks.overviewPolyline
                
                SearchedRoute.searchedRoute = Route.init(
                    nameOfStartingPoint: originAdress,
                    latitudeOfStartigPoint: latitudeOfOrigin,
                    longitudeOfStartingPoint: longitudeOfOrigin,
                    nameOfEndpoint: destinationAdress,
                    latitudeOfEndPoint: latitudeOfDestination,
                    longitudeOfEndPoint: longititudeOfDestination,
                    overviewPolyline: overviewPolyline,
                    date: date,
                    time: time,
                    driver: Home.UserConnectedInformations.user.id,
                    recurrence: recurrence,
                    distance: distance,
                    duration: duration
                )
                DispatchQueue.main.async() {
                    self.performSegue(withIdentifier: "RouteListSegue", sender: nil)
                }
            } else {
                // Afficher une erreur
            }
        })
        
    }
    
    @objc func keyboardWillShow(notification: NSNotification) {
        if ((notification.userInfo?[UIKeyboardFrameBeginUserInfoKey] as? NSValue)?.cgRectValue) != nil {
            if self.view.frame.origin.y == 0{
                self.view.frame.origin.y -= 50
            }
        }
    }
    
    
    @objc func keyboardWillHide(notification: NSNotification) {
        if ((notification.userInfo?[UIKeyboardFrameBeginUserInfoKey] as? NSValue)?.cgRectValue) != nil {
            if self.view.frame.origin.y != 0{
                self.view.frame.origin.y += 50
            }
        }
    }
    
    
    @IBAction func textFieldEditingDate(sender: UITextField) {
        let datePickerView:UIDatePicker = UIDatePicker()
        
        datePickerView.datePickerMode = UIDatePickerMode.date
        sender.inputView = datePickerView
        datePickerView.addTarget(self, action: #selector(self.datePickerChanged), for: UIControlEvents.valueChanged)
        
        let toolbar = UIToolbar()
        toolbar.barStyle = UIBarStyle.blackTranslucent
        toolbar.tintColor = UIColor.white
        toolbar.sizeToFit()
        
        let todayButton = UIBarButtonItem(title: "Now", style: UIBarButtonItemStyle.plain, target: self, action: #selector(SearchRoute.todayDatePressed(sender:)) )
        let spaceButton = UIBarButtonItem(barButtonSystemItem: UIBarButtonSystemItem.flexibleSpace, target: self, action: nil)
        let doneButton = UIBarButtonItem(title: "Done", style: UIBarButtonItemStyle.done, target: self, action: #selector(SearchRoute.dateDonePressed(sender:)) )
        
        toolbar.setItems([todayButton, spaceButton, doneButton], animated: false)
        toolbar.isUserInteractionEnabled = true
        sender.inputAccessoryView = toolbar
    }
    
    
    
    @IBAction func textFieldEditingTime(sender: UITextField) {
        let datePickerView:UIDatePicker = UIDatePicker()
        datePickerView.datePickerMode = UIDatePickerMode.time
        sender.inputView = datePickerView
        datePickerView.addTarget(self, action: #selector(self.timePickerChanged), for: UIControlEvents.valueChanged)
        
        let toolbar = UIToolbar()
        toolbar.barStyle = UIBarStyle.blackTranslucent
        toolbar.tintColor = UIColor.white
        toolbar.sizeToFit()
        
        let todayButton = UIBarButtonItem(title: "Now", style: UIBarButtonItemStyle.plain, target: self, action: #selector(SearchRoute.todayTimePressed(sender:)) )
        let spaceButton = UIBarButtonItem(barButtonSystemItem: UIBarButtonSystemItem.flexibleSpace, target: self, action: nil)
        let doneButton = UIBarButtonItem(title: "Done", style: UIBarButtonItemStyle.done, target: self, action: #selector(SearchRoute.timeDonePressed(sender:)) )
        
        toolbar.setItems([todayButton, spaceButton, doneButton], animated: false)
        toolbar.isUserInteractionEnabled = true
        sender.inputAccessoryView = toolbar
    }
    
    @IBAction func autoOnOff (sender : UISwitch) {
        affichageJour = sender.isOn    //On attribue à modeAuto la valeur du UISwitch
    }
    
    
    @nonobjc func locationManager(manager: CLLocationManager!, didChangeAuthorizationStatus status: CLAuthorizationStatus) {
        if status == CLAuthorizationStatus.authorizedWhenInUse {
            locationManager.startUpdatingLocation()
            viewMap?.isMyLocationEnabled = true
        }
    }
    
    // For indicate the current location
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        let userLocation = locations.last
        
        self.myLocationLat = Float(userLocation!.coordinate.latitude)
        self.myLocationLng = Float(userLocation!.coordinate.longitude)
        
        viewMap?.isMyLocationEnabled = true
        locationManager.stopUpdatingLocation()
    }
    
    
    @objc func timePickerChanged(sender: UIDatePicker) {
        let timeFormatter = DateFormatter()
        timeFormatter.timeStyle = DateFormatter.Style.short
        hourTextField.text = timeFormatter.string(for: sender.date)
    }
    
    @objc func datePickerChanged(sender: UIDatePicker) {
        
        let dateFormatter = DateFormatter()
        //dateFormatter.dateStyle = DateFormatter.Style.short
        dateFormatter.dateFormat = "YYYY-MM-dd"
        dateTextField.text = dateFormatter.string(for: sender.date)
    }
    
    @objc func todayDatePressed(sender: Any){
        let currentDate = Date()
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "YYYY-MM-dd"
        dateTextField.text = dateFormatter.string(for: currentDate)
        dateTextField.resignFirstResponder()
    }
    
    @objc func todayTimePressed(sender: Any){
        let currentTime = Date()
        let timeFormatter = DateFormatter()
        timeFormatter.timeStyle = DateFormatter.Style.short
        hourTextField.text = timeFormatter.string(for: currentTime)
        hourTextField.resignFirstResponder()
    }
    
    @objc func dateDonePressed(sender: UIBarButtonItem){
        dateTextField.resignFirstResponder()
    }
    
    @objc func timeDonePressed(sender: UIBarButtonItem){
        hourTextField.resignFirstResponder()
    }
    
    
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        self.view.endEditing(true)
    }
    
    
}
