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

        //Envoie d'un mail
        //On crée un composeur de mails
        let mc = MFMailComposeViewController()
        //On lui donne son "délégué"
        mc.mailComposeDelegate = self
        //On donne des destinataires au mail
        mc.setToRecipients(["contact@firstapp.com"])
        //On donne un sujet
        mc.setSubject("From my first app")
        //Et on peut même écrire le corps du texte
        mc.setMessageBody("Hi there,\n this is a fake body for my email", isHTML: false)
        //On le montre
        self.present(mc, animated: true, completion: nil)
                
    }
    
    // Fonction pour ouvrir le navigateur internet avec un site internet, mais on a pas de site pour l'intant
    @IBAction func openBrowser(sender: Any){
        UIApplication.shared.open(NSURL(string: "https://firstapp.com")! as URL)
    }
    
    
    // Appelée lorsque l'utilisateur a fini avec ses mails
    func mailComposeController(_ controller: MFMailComposeViewController, didFinishWith result: MFMailComposeResult, error: Error?) {
        controller.dismiss(animated: true, completion: nil)
    }
}
