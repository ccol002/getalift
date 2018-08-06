//
//  HomePage.swift
//  GALDev
//
//  Created by Loan Aubergeon on 29/10/2017.
//  Copyright © 2017 Loan Aubergeon. All rights reserved.
//

import Foundation

/// The home page when the user open the application.
class HomePage : UIViewController {
    
    //  #################### Functions ####################
    
    /// Function to access the login page
    @IBAction func goToLoginPage (sender: Any){
        // get parent view controller
        let parentVC = self.parent as! PageViewController
        
        // change page of PageViewController
        parentVC.setViewControllers([parentVC.orderedViewControllers[1]], direction: .forward, animated: true, completion: nil)
    }
    
    /// Function to access the create a account page
    @IBAction func goToCreateAAccountPage (sender: Any){
        // get parent view controller
        let parentVC = self.parent as! PageViewController
        
        // change page of PageViewController
        parentVC.setViewControllers([parentVC.orderedViewControllers[2]], direction: .forward, animated: true, completion: nil)
    }
    
}
