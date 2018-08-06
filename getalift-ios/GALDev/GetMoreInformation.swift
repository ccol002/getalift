//
//  GetMoreInformation.swift
//  GALDev
//
//  Created by administrator on 31/10/2017.
//  Copyright © 2017 Loan Aubergeon. All rights reserved.
//


import UIKit
import MessageUI

class GetMoreInformation: UIViewController, MFMailComposeViewControllerDelegate {
    
    
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
    
    @IBAction func openWeb (sender: Any){
        guard let url = URL(string: "http://laubergeon.fr") else {
            return
        }
        
        if #available(iOS 10.0, *) {
            UIApplication.shared.open(url, options: [:], completionHandler: nil)
        } else {
            UIApplication.shared.openURL(url)
        }
    }
    
    
    @IBAction func mailCompose (sender: Any){
        //Envoie d'un mail
        //On crée un composeur de mails
        let mc = MFMailComposeViewController()
        //On lui donne son "délégué"
        mc.mailComposeDelegate = self
        //On donne des destinataires au mail
        mc.setToRecipients(["laubergeon@gmail.com"])
        //On donne un sujet
        mc.setSubject("Informations about GetALift")
        //Et on peut même écrire le corps du texte
        mc.setMessageBody("...", isHTML: false)
        //On le montre
        self.present(mc, animated: true, completion: nil)
        
    }
    
    // Appelée lorsque l'utilisateur a fini avec ses mails
    func mailComposeController(_ controller: MFMailComposeViewController, didFinishWith result: MFMailComposeResult, error: Error?) {
        controller.dismiss(animated: true, completion: nil)
    }
    
    
}
