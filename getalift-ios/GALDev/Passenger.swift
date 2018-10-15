//
//  Passenger.swift
//  GALDev
//
//  Created by Charly Joncheray on 18/09/2018.
//  Copyright © 2018 Loan Aubergeon. All rights reserved.
//

import Foundation

struct Passenger {
    
    var id: Int
    var ride: Int
    var passenger: Int
    var inTheCar: Int
    var username: String
    
    init(id : Int, ride: Int, passenger: Int, inTheCar: Int, username: String) {
        self.id = id
        self.ride = ride
        self.passenger = passenger
        self.inTheCar = inTheCar
        self.username = username
    }
    
}
