//
//  Menu.swift
//  GALDev
//
//  Created by Loan Aubergeon on 30/08/2017.
//  Copyright © 2017 Loan Aubergeon. All rights reserved.
//

import UIKit
import MessageUI

class Menu : UITableViewController, MFMailComposeViewControllerDelegate {
    
    
     //  #################### Functions ####################
    
    /// Function to open the mail composer
    @IBAction func mailCompose (sender: Any){

        //Send an email
        //We create an email composer
        let mc = MFMailComposeViewController()
        //We give him his "delegate"
        mc.mailComposeDelegate = self
        //We give recipients to the mail
        mc.setToRecipients(["contact@firstapp.com"])
        //We give a subject
        mc.setSubject("From my first app")
        //And we can even write the body of the text
        mc.setMessageBody("Hi there,\n this is a fake body for my email", isHTML: false)
        //We show it
        self.present(mc, animated: true, completion: nil)
                
    }
    
    // Function to open the internet browser with a website, but we have no site for intant
    @IBAction func openBrowser(sender: Any){
        UIApplication.shared.open(NSURL(string: "https://firstapp.com")! as URL)
    }
    
    
    // Called when the user has finished with his mails
    func mailComposeController(_ controller: MFMailComposeViewController, didFinishWith result: MFMailComposeResult, error: Error?) {
        controller.dismiss(animated: true, completion: nil)
    }
}
