//
//  Rides.swift
//  GALDev
//
//  Created by Charly Joncheray on 26/09/2018.
//  Copyright © 2018 Loan Aubergeon. All rights reserved.
//

import UIKit

class Rides: UITableViewController {

    // ---- PROPEERTIES ----
    var rides = [Route]()
    
    // ---- TASKS ----
    var ridesTasks = RideTasks()
    var userTasks = UserTasks()
    var routeTasks = RouteTasks()
    var dateTasks = DateTasks()
    
    // ---- FUNCTIONS ----
    
    override func viewDidLoad() {
        super.viewDidLoad()

        tableView.rowHeight = 87
        
        let userId = Home.UserConnectedInformations.user.id
        
        ridesTasks.rides(passengerId: userId!) { (status, success) in
            if success {
                self.rides = self.ridesTasks.rides
                DispatchQueue.main.async {
                    self.tableView.reloadData()
                }
            }
        }
        
    }

    // MARK: - Table view data source

    override func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }

    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // #warning Incomplete implementation, return the number of rows
        return rides.count
    }
    
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cellIdentifier = "RidesTableViewCell"
        
        guard let cell = tableView.dequeueReusableCell(withIdentifier: cellIdentifier, for: indexPath) as? RidesTableViewCell else {
            fatalError("Erreur de conversion")
        }
        
        let ride = rides[indexPath.row]
        
        cell.origin.text = ride.nameOfStartingPoint
        cell.destination.text = ride.nameOfEndpoint
        
        let driverId = ride.driver
        
        self.userTasks.user(driverId: driverId) { (status, success) in
            if success {
                DispatchQueue.main.async {
                    cell.driver.text = self.userTasks.user.surname
                }
            }
        }
        
        let routeId = ride.id
        
        self.dateTasks.date(routeId: routeId) { (status, success) in
            if success {
                DispatchQueue.main.async {
                    cell.date.text = self.dateTasks.date
                }
            }
        }
        
        return cell
    }
    
    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        //tableView.deselectRow(at: indexPath, animated: true)
         myIndex = indexPath.row
        performSegue(withIdentifier: "segueFromRidesToDriverView", sender: self)
        
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "segueFromRidesToDriverView" {
            if let destination = segue.destination as? RouteViewForTheMenu {
                destination.routes = self.rides
                destination.ridesSegue = true
            }
        }
    }
    
    
    
    
    
    
    
}
