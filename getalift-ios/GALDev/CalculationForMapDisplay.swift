//
//  CalculationForMapDisplay.swift
//  GALDev
//
//  Created by Loan Aubergeon on 06/10/2017.
//  Copyright © 2017 Loan Aubergeon. All rights reserved.
//

import Foundation

class CalculationForMapDisplay{
    
    var xCenter : Double = 0
    var yCenter : Double = 0
 
    var zoom : Int!
    
    func zoomCalcul(distance : Double) -> Float{
        var zoom : Float!
        
        if distance > 2000 {
            zoom = 2
        } else if distance > 1500 {
            zoom = 4
        } else if distance > 1000 {
            zoom = 5
        } else if distance > 800 {
            zoom = 5.5
        }else if distance > 500 {
            zoom = 6
        }else if distance > 400 {
            zoom = 7
        }else if distance > 300 {
            zoom = 7.3
        }else if distance > 200 {
            zoom = 7.5
        }else if distance > 100 {
            zoom = 8.5
        }else if distance > 50 {
            zoom = 9
        }else if distance > 35 {
            zoom = 10
        }else if distance > 20 {
            zoom = 11
        }else if distance > 10 {
            zoom = 12
        }else if distance > 5 {
            zoom = 13
        }else if distance > 1 {
            zoom = 14
        } else {
            zoom = 15
        }
        
        return zoom
    }
    
    func centerCalcul(xA : Double, yA : Double, xB : Double, yB : Double){
        
        let difX : Double = abs(xA - xB)/2
        let difY : Double = abs(yA - yB)/2
        
        if (xA == xB){
            xCenter = xA
            if yA == yB {
                yCenter = yA
            } else if (yB > yA){
                yCenter = yA + difY
            } else {
                yCenter = yB + difY
            }
            
        }else if (xB > xA){
            xCenter = xA + difX
            if yA == yB {
                yCenter = yA
            } else if (yB > yA){
                yCenter = yA + difY
            } else {
                yCenter = yB + difY
            }
        } else {
            xCenter = xB + difX
            if yA == yB {
                yCenter = yA
            } else if (yA > yB) {
                yCenter = yB + difY
            } else {
                yCenter = yA + difY
            }
        }
    }
    
    
}
