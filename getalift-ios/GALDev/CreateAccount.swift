//
//  CreateAccount.swift
//  GetALiftDev
//
//  Created by Loan Aubergeon on 17/08/2017.
//  Copyright © 2017 Loan Aubergeon. All rights reserved.
//

import UIKit
import NotificationBannerSwift

/// View for create on account one the database
class CreateAccount : UIViewController {
    
    //  #################### Variables ####################
    
    /// TexteField to retrieve User's informations
    @IBOutlet var usernameF : UITextField!
    @IBOutlet var passwordF : UITextField!
    @IBOutlet var nameF : UITextField!
    @IBOutlet var surnameF : UITextField!
    @IBOutlet var emailF : UITextField!
    @IBOutlet var mobileNumberF : UITextField!
    
    
    //  #################### Functions ####################
    
    /// Function to access the login page
    @IBAction func goToLoginPage (sender: Any){
        // get parent view controller
        let parentVC = self.parent as! PageViewController
        
        // change page of PageViewController
        parentVC.setViewControllers([parentVC.orderedViewControllers[1]], direction: .reverse, animated: true, completion: nil)
    }
    
    override func viewDidLoad() {
        NotificationCenter.default.addObserver(self, selector: #selector(self.keyboardWillShow), name: NSNotification.Name.UIKeyboardWillShow, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(self.keyboardWillHide), name: NSNotification.Name.UIKeyboardWillHide, object: nil)
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        NotificationCenter.default.removeObserver(self, name: NSNotification.Name.UIKeyboardWillShow, object: self.view.window)
        NotificationCenter.default.removeObserver(self, name: NSNotification.Name.UIKeyboardWillHide, object: self.view.window)
    }
    
    /// The requeste on the database
    @IBAction func createAccount (sender:UIButton){
        
        // User's informations are retrieved, we delete gaps and comas
        let username = usernameF.text?.replacingOccurrences(of:" ", with: "").replacingOccurrences(of: ",", with: "")
        let password = passwordF.text?.replacingOccurrences(of:" ", with: "").replacingOccurrences(of: ",", with: "")
        let name = nameF.text?.replacingOccurrences(of:" ", with: "").replacingOccurrences(of: ",", with: "")
        let surname = surnameF.text?.replacingOccurrences(of:" ", with: "").replacingOccurrences(of: ",", with: "")
        let email = emailF.text?.replacingOccurrences(of:" ", with: "").replacingOccurrences(of: ",", with: "")
        let mobileNumber = mobileNumberF.text?.replacingOccurrences(of:" ", with: "").replacingOccurrences(of: ",", with: "")
        
        // String with user's informations for the database
        let account = "username="+username!+"&password="+password!+"&name="+name!+"&surname="+surname!+"&email="+email!+"&mobileNumber="+mobileNumber!
        
        // URL
        let url = NSURL(string: ServerAdress+":3000/api/users")!
        
        // Request
        var request = URLRequest(url: url as URL)
        
        // All textfield must be completed
        if (username != "") && (password != "") && (name != "") && (surname != "") && (email != "") && (mobileNumber != ""){
            
            do {
                // Set the request content type to JSON
                request.setValue("application/x-www-form-urlencoded", forHTTPHeaderField: "Content-Type")
            
                // The magic...set the HTTP request method to POST
                request.httpMethod = "POST"
            
                // Add the JSON serialized login data to the body
                request.httpBody = account.data(using: String.Encoding.utf8)
            
            
                // Execute the request
                let task = URLSession.shared.dataTask(with: request as URLRequest) {
                    data, response, error in
                
                    // Check for error
                    if error != nil
                    {
                        print("Error")
                        
                        DispatchQueue.main.async() {
                            let imageView = UIImageView(image: #imageLiteral(resourceName: "failed"))
                            let banner = NotificationBanner(title: "Error", subtitle: "Bad connection", leftView: imageView, style: .danger)
                            banner.show()
                        }
                    }
                    // Convert server json response to NSDictionary
                    do {
                        let json = try JSONSerialization.jsonObject(with: data!, options: .mutableContainers) as? NSDictionary
                    
                        if let parseJSON = json {
                            DispatchQueue.main.async() { // Permet de mettre a jour l'UI sans attendre la fin du task
                            
                                let success = parseJSON["success"] as? Bool
                                // Display an alert if the user has been created
                                if success! {
                                    let storyboard = UIStoryboard(name: "Main", bundle: nil)
                                    let homePage = storyboard.instantiateViewController(withIdentifier: "transitionPage") as! SWRevealViewController
                                    self.present(homePage, animated: true, completion: { () -> Void in
                                        DispatchQueue.main.async() {
                                            let imageView = UIImageView(image: #imageLiteral(resourceName: "success"))
                                            let banner = NotificationBanner(title: "Succes", subtitle: "You have created your account", leftView: imageView, style: .success)
                                            banner.show()
                                        }
                                    })
                                }
                            }
                        }
                    } catch let error as NSError {
                        print(error.localizedDescription)
                    }
                }
                // We execute the task
                task.resume()
            }
        } else {
            // Display a error when all fields are not completed
            DispatchQueue.main.async() {
                let imageView = UIImageView(image: #imageLiteral(resourceName: "failed"))
                let banner = NotificationBanner(title: "Error", subtitle: "Please field all fields", leftView: imageView, style: .danger)
                banner.show()
            }
            
        }
    }
    
    /// To move the screen when the keyboard is displayed
    @objc func keyboardWillShow(notification: NSNotification) {
        if ((notification.userInfo?[UIKeyboardFrameBeginUserInfoKey] as? NSValue)?.cgRectValue) != nil {
            if self.view.frame.origin.y == 0{
                self.view.frame.origin.y -= 150
            }
        }
    }
    
    
    @objc func keyboardWillHide(notification: NSNotification) {
        if ((notification.userInfo?[UIKeyboardFrameBeginUserInfoKey] as? NSValue)?.cgRectValue) != nil {
            if self.view.frame.origin.y != 0{
                self.view.frame.origin.y += 150
            }
        }
    }
    
    /// Function for display a alerte
    func errorAlert(title: String, message: String){
        let alertController = UIAlertController(title: title, message: message, preferredStyle: .alert)
        let defaultAction = UIAlertAction(title: "Ok", style: .default, handler: nil)
        alertController.addAction(defaultAction)
        present(alertController, animated: true, completion: nil)
    }
    /// Close the keyboard when user tap on the screen
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        self.view.endEditing(true)
    }
    
}
