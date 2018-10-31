//
//  DriverView.swift
//  GALDev
//
//  Created by Loan Aubergeon on 18/09/2017.
//  Copyright © 2017 Loan Aubergeon. All rights reserved.
//

import UIKit
import MessageUI
import NotificationBannerSwift
import Cosmos

/// Class to display information of the driver of the selected route
class DriverView : UIViewController, MFMailComposeViewControllerDelegate, MFMessageComposeViewControllerDelegate {
    
    //  #################### Variables ####################

    var userTasks = UserTasks()
    var routeTasks = RouteTasks()
    var favoriteRouteTasks = FavoriteRouteTasks()
    
    /// User's token
    var token = Home.UserConnectedInformations.userToken
    
    /// Driver's informations to call or send a email
    var driverEmail = ""
    var mobileNumber = ""
    
    var routes : [Route] = []
    
    var searchedRoute : Route = SearchRoute.SearchedRoute.searchedRoute
    
    var ridesSegue: Bool = false
    
    /// Driver's informations
    @IBOutlet var firstNameLabel : UILabel!
    @IBOutlet var lastNameLabel : UILabel!
    @IBOutlet var usernameLabel : UILabel!
    @IBOutlet var mobileNumberLabel : UILabel!
    @IBOutlet var emailLabel : UILabel!
    
    @IBOutlet weak var cosmosView: CosmosView!
    
     //  #################### Functions ####################
    
    override func viewDidLoad() {
        let driverId = self.routes[myIndex].driver
        
        self.userTasks.user(driverId: driverId, completionHandler: { (status, success) -> Void in
            if success {
                DispatchQueue.main.async() {
                    self.firstNameLabel?.text = self.userTasks.user.name
                    self.lastNameLabel?.text = self.userTasks.user.surname
                    self.usernameLabel?.text = self.userTasks.user.username
                    self.mobileNumberLabel?.text = self.userTasks.user.mobileNumber
                    self.emailLabel?.text = self.userTasks.user.email
                }
                self.driverEmail = self.userTasks.user.email
                self.mobileNumber = self.userTasks.user.mobileNumber
                
                let rateButtonItem = UIBarButtonItem.init(
                    image: #imageLiteral(resourceName: "podium"),
                    style: .done,
                    target: self,
                    action: #selector(self.displayRatingView(sender:))
                )
                
                if self.ridesSegue == true {
                     self.navigationItem.rightBarButtonItems = [rateButtonItem]
                }
                
            }
        })
        
        cosmosView.settings.updateOnTouch = false
        self.ratingTask.getRating(targetId: driverId!, completitionHandler: { (status, sucess) -> Void in
            if sucess {
                DispatchQueue.main.async {
                    self.rate = self.ratingTask.rate
                    self.updateRating()
                }
            }
        })
    }
    
    @IBAction func displayRatingView(sender: AnyObject) {
        performSegue(withIdentifier: "rateSegue", sender: self)
    }
    
    @IBAction func addToFavoriteRoute(sender: AnyObject){
        let userId = Home.UserConnectedInformations.user.id
        let routeId = self.routes[myIndex].id
        
        self.favoriteRouteTasks.favoriteRoute(routeId: routeId!, userId: userId!, completionHandler: { (status, success) -> Void in
            if status == "Existing" {
                DispatchQueue.main.async() {
                    let imageView = UIImageView(image: #imageLiteral(resourceName: "failed"))
                    let banner = NotificationBanner(title: "Already favorite", subtitle: "The route is already in your favorite routes", leftView: imageView, style: .warning)
                    banner.show()
                }
            } else if status == "No existing" {
                self.favoriteRouteTasks.addFavoriteRoute(userId: userId!, routeId: routeId!, completionHandler: { (status, success) -> Void in
                    if success {
                        DispatchQueue.main.async() {
                            let imageView = UIImageView(image: #imageLiteral(resourceName: "success"))
                            let banner = NotificationBanner(title: "Route added", subtitle: "The route has been added in your favorite", leftView: imageView, style: .success)
                            banner.show()
                        }
                    } else {
                        DispatchQueue.main.async() {
                            let imageView = UIImageView(image: #imageLiteral(resourceName: "failed"))
                            let banner = NotificationBanner(title: "Route didn't add", subtitle: "The route hasn't been add to your favorite routes", leftView: imageView, style: .danger)
                            banner.show()
                            
                        }
                        
                    }
                })
            } else {
                DispatchQueue.main.async() {
                    self.alert(title: "Error", message: "")
                }
            }
        })
    }
    
    
    @IBAction func sendMessage(sender: AnyObject) {
        let messageVC = MFMessageComposeViewController()
        
        messageVC.body = "Hello, I am interested for traveling this route with you";
        messageVC.recipients = [self.mobileNumber]
        messageVC.messageComposeDelegate = self;
        
        self.present(messageVC, animated: false, completion: nil)
    }
    
    func messageComposeViewController(_ controller: MFMessageComposeViewController, didFinishWith result: MessageComposeResult) {
        switch (result.rawValue) {
        case MessageComposeResult.cancelled.rawValue:
            print("Message was cancelled")
            self.dismiss(animated: true, completion: nil)
        case MessageComposeResult.failed.rawValue:
            print("Message failed")
            self.dismiss(animated: true, completion: nil)
        case MessageComposeResult.sent.rawValue:
            print("Message was sent")
            self.dismiss(animated: true, completion: nil)
        default:
            break;
        }
    }
    
    func configureMailController() -> MFMailComposeViewController {
        let mailComposerVC = MFMailComposeViewController()
        mailComposerVC.mailComposeDelegate = self
        
        mailComposerVC.setToRecipients([driverEmail])
        mailComposerVC.setSubject("From my first app")
        mailComposerVC.setMessageBody("Hi there, \n I am interested in your route", isHTML: false)
        
        return mailComposerVC
    }
    
    func showMailError() {
        let sendMailErrorAlert = UIAlertController(title: "Could not send email", message: "Your device could not send email", preferredStyle: .alert)
        let dismiss = UIAlertAction(title: "Ok", style: .default, handler: nil)
        sendMailErrorAlert.addAction(dismiss)
        self.present(sendMailErrorAlert, animated: true, completion: nil)
    }
    
    @IBAction func mailCompose (_ sender: Any){
        
        let mailComposeViewController = configureMailController()
        if MFMailComposeViewController.canSendMail() {
            self.present(mailComposeViewController, animated: true, completion: nil)
        } else {
            showMailError()
        }
        
    }
    
    // Appelée lorsque l'utilisateur a fini avec ses mails
    func mailComposeController(_ controller: MFMailComposeViewController, didFinishWith result: MFMailComposeResult, error: Error?) {
        controller.dismiss(animated: true, completion: nil)
    }
    
    @IBAction func phoneCompose (sender: Any){
        if let url = URL(string: "tel://\(mobileNumber)"), UIApplication.shared.canOpenURL(url) {
            if #available(iOS 10, *) {
                UIApplication.shared.open(url)
            } else {
                UIApplication.shared.openURL(url)
            }
        }
    }
    
    @IBAction func unwindToDriverView(segue:UIStoryboardSegue) { }
    
    func alert(title: String, message: String){
        let alertController = UIAlertController(title: title, message: message, preferredStyle: .alert)
        let defaultAction = UIAlertAction(title: "Ok", style: .default, handler: nil)
        alertController.addAction(defaultAction)
        present(alertController, animated: true, completion: nil)
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "commentsViewSegue" {
            if let destination = segue.destination as? Comments {
                destination.routes = self.routes
            }
        } else if segue.identifier == "rateSegue" {
            if let destination = segue.destination as? Rating {
                destination.routes = self.routes
            }
        }
    }
    
    //---------Rating System--------
    
    var rate: Float = 0
    
    var ratingTask = RatingTasks()
    
    private func updateRating() {
        cosmosView.rating = Double(self.rate)
    }
    
}
