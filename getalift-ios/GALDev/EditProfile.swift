//
//  EditProfile.swift
//  GALDev
//
//  Created by Charly Joncheray on 09/10/2018.
//  Copyright © 2018 Loan Aubergeon. All rights reserved.
//

import UIKit
import NotificationBannerSwift

class EditProfile: UIViewController {

    //Mark: Variables
    var user : User = Home.UserConnectedInformations.user
    
    var authentificationConfirm: Bool = false
    
    //Mark: Tasks
    var userTasks = UserTasks()
    
    //Mark: Outlets
    @IBOutlet weak var lastName: UITextField!
    @IBOutlet weak var firstName: UITextField!
    @IBOutlet weak var username: UITextField!
    @IBOutlet weak var phone: UITextField!
    @IBOutlet weak var email: UITextField!
    
    @IBOutlet weak var actualPassword: UITextField!
    @IBOutlet weak var newPassword: UITextField!
    @IBOutlet weak var confirmNewPassword: UITextField!
    
    
    //Mark: function
    override func viewDidLoad() {
        super.viewDidLoad()

        self.lastName.placeholder = user.surname
        self.firstName.placeholder = user.name
        self.username.placeholder = user.username
        self.phone.placeholder = user.mobileNumber
        self.email.placeholder = user.email
        
    }
    
    private func editInformations() {
        
        var username: String = ""
        var name: String = ""
        var surname: String = ""
        var email: String = ""
        var mobileNumber: String = ""
        var password: String = ""
        
        //Username
        if self.lastName.text == "" {
            surname = user.surname
        } else {
            surname = self.lastName.text!
        }
        
        //name
        if self.firstName.text == "" {
            name = user.name
        } else {
            name = self.firstName.text!
        }
        
        //surname
        if self.username.text == "" {
            username = user.username
        } else {
            username = self.username.text!
        }
        
        //email
        if self.email.text == "" {
            email = user.email
        } else {
            email = self.email.text!
        }
        
        //mobileNumber
        if self.phone.text == "" {
            mobileNumber = user.mobileNumber
        } else {
            mobileNumber = self.phone.text!
        }
        
        
        
        if self.actualPassword.text != "" {
            self.userTasks.authentification(username: user.username, password: actualPassword.text!) { (status, success) in
                if success {
                        if self.newPassword.text != "" || self.confirmNewPassword.text != "" {
                            if self.newPassword.text != self.confirmNewPassword.text {
                                let imageView = UIImageView(image: #imageLiteral(resourceName: "failed"))
                                let banner = NotificationBanner(title: "Error", subtitle: "New password is not the same in the 2 fields", leftView: imageView, style: .danger)
                                banner.show()
                            } else {
                                password = self.newPassword.text!
                            }
                        } else {
                            password = self.actualPassword.text!
                        }
                        
                        if (self.newPassword.text == self.confirmNewPassword.text) || (self.newPassword.text! == "" && self.confirmNewPassword.text! == "") {
                            self.userTasks.editUser(driverId: self.user.id, username: username, password: String(password), name: name, surname: surname, email: email, mobileNumber: mobileNumber) { (status, success) in
                                if success {
                                    
                                    DispatchQueue.main.async() {
                                        /// Recovery Main.storyboard
                                        let storyboard = UIStoryboard(name: "Main", bundle: nil)
                                        
                                        /// Create a transition page to access the main page
                                        let transitionPage = storyboard.instantiateViewController(withIdentifier: "transitionPage") as! SWRevealViewController
                                        
                                        /// Acces to the main page
                                        self.present(transitionPage, animated: true, completion: { () -> Void in
                                            DispatchQueue.main.async() {
                                                let imageView = UIImageView(image: #imageLiteral(resourceName: "success"))
                                                let banner = NotificationBanner(title: "Successful", subtitle: "Your profil was edited with success", leftView: imageView, style: .success)
                                                banner.show()
                                            }
                                        })
                                    }
                                } else {
                                    DispatchQueue.main.async {
                                        let imageView = UIImageView(image: #imageLiteral(resourceName: "failed"))
                                        let banner = NotificationBanner(title: "Bad connection", subtitle: "Please retry", leftView: imageView, style: .danger)
                                        banner.show()
                                    }
                                }
                            }
                    } else {
                        let imageView = UIImageView(image: #imageLiteral(resourceName: "failed"))
                        let banner = NotificationBanner(title: "Not the good password", subtitle: "Your password is not correct", leftView: imageView, style: .danger)
                        banner.show()
                    }
                } else {
                    let imageView = UIImageView(image: #imageLiteral(resourceName: "failed"))
                    let banner = NotificationBanner(title: "Bad password", subtitle: "Not the good password", leftView: imageView, style: .danger)
                    banner.show()
                }
            }
        } else {
            let imageView = UIImageView(image: #imageLiteral(resourceName: "failed"))
            let banner = NotificationBanner(title: "No password", subtitle: "Please enter your password", leftView: imageView, style: .danger)
            banner.show()
        }
    }
    
    //Mark: -Actions
    @IBAction func editInformationsButtonPressed(_ sender: UIButton) {
        return editInformations()
    }
    

}
