//
//  routeSaved.swift
//  GALDev
//
//  Created by Loan Aubergeon on 23/10/2017.
//  Copyright © 2017 Loan Aubergeon. All rights reserved.
//

import Foundation
import NotificationBannerSwift

class routeSaved : UIViewController, UITableViewDataSource, UITableViewDelegate {
    
    
    //  #################### Variables ####################

    @IBOutlet weak var menuButton:UIBarButtonItem!
    
    /// User's token
    var token = Home.UserConnectedInformations.userToken
    
    /// Table for show the list of available route
    @IBOutlet var routeTableView : UITableView!
    
    /// Data array for show routes one by one
    var routes : [Route] = []
    
    /// Differents tasks
    var mapTasks = MapTasks()
    var userTasks = UserTasks()
    var dateTasks = DateTasks()
    var favoriteRouteTasks = FavoriteRouteTasks()
    
    var refreshControl: UIRefreshControl!
    
    
     //  #################### Functions ####################
    
    override func viewDidLoad() {
        
        super.viewDidLoad()
        if self.revealViewController() != nil {
            menuButton.target = self.revealViewController()
            menuButton.action = #selector(SWRevealViewController.revealToggle(_:))
            self.view.addGestureRecognizer(self.revealViewController().panGestureRecognizer())
        }
        
        routeTableView.dataSource = self
        routeTableView.delegate = self
        
        let userId = Home.UserConnectedInformations.user.id
        
        self.favoriteRouteTasks.favoriteRoute(userId : userId!, completionHandler: { (status, success) -> Void in
            if success {
                self.routes = self.favoriteRouteTasks.routes
                DispatchQueue.main.async {
                    self.routeTableView.reloadData()
                }

            }
        })
        
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
            emptyStateLabel.text = "You don't have favorite routes ! \n Add one to see it"
            emptyStateLabel.textAlignment = NSTextAlignment.center
            tableView.backgroundView = emptyStateLabel
            return 0
        } else {
            tableView.backgroundView = nil
            return self.routes.count
        }
    }
    /*func tableView(_ tableView: UITableView, titleForHeaderInSection section: Int) -> String? {
     switch section {
     case 0: return "Routes : "
     default: return ""
     }
     }*/
    
    
    //Cellule à l'index concerné
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "cell") as! MyCustomCell
        
        for i in 0...self.routes.count {
            
            if (indexPath.row == i) {
                DispatchQueue.main.async {
                    cell.originLabel.text = self.routes[i].nameOfStartingPoint
                    cell.destinationLabel.text = self.routes[i].nameOfEndpoint
                    
                    let routeId : Int = self.routes[i].id
                    self.dateTasks.date(routeId: routeId, completionHandler: { (status, success) -> Void in
                        if success {
                            DispatchQueue.main.async {
                                cell.dateLabel.text = self.dateTasks.date
                                cell.reccurence.isHidden = !self.dateTasks.weeklyReccurence
                                
                                let id = self.routes[i].driver
                                self.userTasks.user(driverId: id, completionHandler: { (status, success) -> Void in
                                    if success {
                                        DispatchQueue.main.async {
                                            cell.driverLabel.text = self.userTasks.user.username
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
    func tableView(_ tableView: UITableView, canEditRowAt indexPath: IndexPath) -> Bool {
        return true
    }
    
    func tableView(_ tableView: UITableView, commit editingStyle: UITableViewCellEditingStyle, forRowAt indexPath: IndexPath) {
        if (editingStyle == UITableViewCellEditingStyle.delete) {
            
            // handle delete (by removing the data from your array and updating the tableview)
            
            // On demande la confirmation avant de supprimer
            let alert = UIAlertController(title: "Delete this route !", message: "Are you sure you want delete this route ?", preferredStyle: .alert)
            
            let ok = UIAlertAction(title: "Cancel", style: UIAlertActionStyle.cancel, handler: nil)
            let delete = UIAlertAction(title: "Delete", style: UIAlertActionStyle.destructive, handler: { action in
                
                let routeId = self.routes[indexPath.row].id
                self.favoriteRouteTasks.deleteFavoriteRoute(routeId: routeId!, completionHandler: { (status, success) -> Void in})
                self.routes.remove(at: indexPath.row)
                self.routeTableView.reloadData()
                let banner = StatusBarNotificationBanner(title: "Route deleted", style: .danger)
                banner.show()
            })
            alert.addAction(delete)
            alert.addAction(ok)
            self.present(alert, animated: true, completion: nil)
            
        }
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        myIndex = indexPath.row
        performSegue(withIdentifier: "segueFromFavoriteToDriverView", sender: self)
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "segueFromFavoriteToDriverView" {
            if let destination = segue.destination as? RouteViewForTheMenu {
                destination.routes = self.routes
            }
        }
    }
}

