//
//  userProfile.swift
//  GALDev
//
//  Created by Loan Aubergeon on 30/08/2017.
//  Copyright © 2017 Loan Aubergeon. All rights reserved.
//

import UIKit


class UserProfile : UIViewController {
    
    
    //  #################### Variables ####################

    @IBOutlet weak var menuButton:UIBarButtonItem!
    
    var user : User = Home.UserConnectedInformations.user
    
    @IBOutlet var firstNameLabel : UILabel!
    @IBOutlet var lastNameLabel : UILabel!
    @IBOutlet var usernameLabel : UILabel!
    @IBOutlet var mobileNumberLabel : UILabel!
    @IBOutlet var emailLabel : UILabel!
    
    
     //  #################### Functions ####################
    
    override func viewDidLoad() {
        super.viewDidLoad()
        if self.revealViewController() != nil {
            menuButton.target = self.revealViewController()
            menuButton.action = #selector(SWRevealViewController.revealToggle(_:))
            self.view.addGestureRecognizer(self.revealViewController().panGestureRecognizer())
        }
        
        firstNameLabel.text = user.name
        lastNameLabel.text = user.surname
        usernameLabel.text = user.username
        mobileNumberLabel.text = user.mobileNumber
        emailLabel.text = user.email
        
    }
}
