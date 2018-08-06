//
//  Date.swift
//  GALDev
//
//  Created by Loan Aubergeon on 05/10/2017.
//  Copyright © 2017 Loan Aubergeon. All rights reserved.
//

import Foundation

struct DateTime {
    
    var routeId : Int!
    var date : String!
    var recurrence : Bool!
    
    init(){
    }
    
    init(routeId : Int, date : String, recurrence : Bool){
        self.routeId = routeId
        self.date = date
        self.recurrence = recurrence
    }
    
}
