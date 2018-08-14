//
//  Settings.swift
//  GALDev
//
//  Created by administrator on 31/10/2017.
//  Copyright © 2017 Loan Aubergeon. All rights reserved.
//

import Foundation

class Settings: UIViewController {
    
    
    //  #################### Variables ####################

    @IBOutlet weak var menuButton:UIBarButtonItem!
    
    
     //  #################### Functions ####################
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        if self.revealViewController() != nil {
            menuButton.target = self.revealViewController()
            menuButton.action = #selector(SWRevealViewController.revealToggle(_:))
            self.view.addGestureRecognizer(self.revealViewController().panGestureRecognizer())
        }
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    
    
}
