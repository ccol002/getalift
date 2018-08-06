//
//  User.swift
//  GALDev
//
//  Created by Loan Aubergeon on 05/10/2017.
//  Copyright © 2017 Loan Aubergeon. All rights reserved.
//

import Foundation

struct User {
    
    var id : Int!
    var username : String!
    var name : String!
    var surname : String!
    var email : String!
    var mobileNumber : String!
    
    init(){
        
    }
    
    init(id: Int, username : String, name: String, surname : String, email : String, mobileNumber: String){
        self.id = id
        self.username = username
        self.name = name
        self.surname = surname
        self.email = email
        self.mobileNumber = mobileNumber
    }
}
