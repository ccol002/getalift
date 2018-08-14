//
//  RouteList.swift
//  GALDev
//
//  Created by Loan Aubergeon on 29/08/2017.
//  Copyright © 2017 Loan Aubergeon. All rights reserved.
//

import UIKit

/// The index of the cell who are selected on the list
var myIndex : Int = 0

/// The page who displayed the list of the route after a research
class RouteList : UIViewController, UITableViewDataSource, UITableViewDelegate {
    
    
    //  #################### Variables ####################

    /// Date about the searched route
    var searchedRoute : Route = SearchRoute.SearchedRoute.searchedRoute
    
    /// User's token
    var token = Home.UserConnectedInformations.userToken
    
    /// Table for show the list of available route
    @IBOutlet var routeTableView : UITableView!
    
    /// Data array for show routes one by one
    var routes : [Route] = []
    
    /// Differents tasks
    var mapTasks = MapTasks()
    var userTasks = UserTasks()
    var routeTasks = RouteTasks()
    var dateTasks = DateTasks()
    var favoriteRouteTasks = FavoriteRouteTasks()
    
    /// Refresh control
    var refreshControl: UIRefreshControl!
    
    
    //  #################### Functions ####################

    override func viewDidLoad() {
        routeTableView.dataSource = self
        routeTableView.delegate = self

        let fullDate : String = self.searchedRoute.date+""+self.searchedRoute.time
        let startLat : Double = self.searchedRoute.latitudeOfStartigPoint
        let startLong : Double = self.searchedRoute.longitudeOfStartingPoint
        let endLat : Double = self.searchedRoute.longitudeOfEndPoint
        let endLong : Double = self.searchedRoute.longitudeOfEndPoint
        
        // Chargement de la liste des routes 
        self.routeTasks.route(date: fullDate, startLat : startLat, startLong : startLong, endLat : endLat, endLong : endLong,  completionHandler: { (status, success) -> Void in
            if success {
                self.routes = self.routeTasks.routes
                
                DispatchQueue.main.async {
                    self.routeTableView.reloadData()
                }
            }
        })
        
        // add pull to refresh
        refreshControl = UIRefreshControl()
        refreshControl.attributedTitle = NSAttributedString(string: "Pull to refresh")
        refreshControl.addTarget(self, action: #selector(self.handleRefresh(_:)), for: UIControlEvents.valueChanged)
        routeTableView.addSubview(refreshControl)
    }
    
    /// Refresh the table view
    @objc func handleRefresh(_ refreshControl: UIRefreshControl) {
        self.routeTableView.reloadData()
        self.refreshControl.endRefreshing()
    }

    
    //Nombre de sections en tout
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if self.routes.count == 0 {
            let emptyStateLabel = UILabel(frame: tableView.frame)
            emptyStateLabel.text = "No routes available !"
            emptyStateLabel.textAlignment = NSTextAlignment.center
            tableView.backgroundView = emptyStateLabel
            return 0
        } else {
            tableView.backgroundView = nil
            return self.routes.count
        }
    }
  

    /// Index cell loading
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "cell") as! MyCustomCell
        
        for i in 0...self.routes.count {
            
            if (indexPath.row == i) {
                /// Update the view
                DispatchQueue.main.async {
                    /// Display the origin and desitination label
                    cell.originLabel.text = self.routes[i].nameOfStartingPoint
                    cell.destinationLabel.text = self.routes[i].nameOfEndpoint
                    /// Display a hearth if the route is favorite
                    cell.favorite.isHidden = true
                    
                    /// The route's Id
                    let routeId : Int = self.routes[i].id
                    
                    /// Task to retrieve the date of the route
                    self.dateTasks.date(routeId: routeId, completionHandler: { (status, success) -> Void in
                        if success {
                            /// Update the view
                            DispatchQueue.main.async {
                                cell.dateLabel.text = self.dateTasks.date
                                cell.reccurence.isHidden = !self.dateTasks.weeklyReccurence
                                
                                /// Id of the driver of the route
                                let id = self.routes[i].driver
                                
                                /// Task to retrieve informations about the driver
                                self.userTasks.user(driverId: id, completionHandler: { (status, success) -> Void in
                                    if success {
                                        
                                        /// Update the view
                                        DispatchQueue.main.async {
                                            cell.driverLabel.text = self.userTasks.user.username
                                            
                                            /// Driver's ID
                                            let userId = Home.UserConnectedInformations.user.id
                                            /// Tasks to know it's a favorite route
                                            self.favoriteRouteTasks.favoriteRoute(routeId: routeId, userId: userId!, completionHandler: { (status, success) -> Void in
                                                if status == "Existing" {
                                                    /// Update the view
                                                    DispatchQueue.main.async {
                                                        cell.favorite.isHidden = false
                                                    }
                                                }
                                            })
                                            
                                        }
                                    }
                                })
                            }
                        }
                    })
                }
            }
        }
        
        return cell
    }
    
    /// for display the next controller to display the route view 
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        myIndex = indexPath.row
        performSegue(withIdentifier: "routeViewSegue", sender: self)
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "routeViewSegue" {
            if let destination = segue.destination as? RouteView {
                destination.routes = self.routes
            }
        }
    }
    
}
