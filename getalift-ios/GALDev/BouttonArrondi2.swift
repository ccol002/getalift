//
//  BouttonArrondi2.swift
//  GALDev
//
//  Created by administrator on 26/10/2017.
//  Copyright © 2017 Loan Aubergeon. All rights reserved.
//

import UIKit

class BouttonArrondi2 : UIButton {
    
    //Intialiseur
    required init?(coder aDecoder: NSCoder) {
        //Initialiseur de la classe parente
        super.init(coder: aDecoder)
        
        //Coins arrondis
        layer.cornerRadius = 10
        
        //Couleur de la bordure
        layer.borderColor = UIColor.gray.cgColor
        
        //Epaisseur de la bordure
        layer.borderWidth = 1
        
        //Couleur du texte
        setTitleColor(UIColor.black, for: .normal)
        
        //Padding a gauche et a droite
        contentEdgeInsets = UIEdgeInsets(top: 5, left: 20, bottom: 5, right: 20)
        
    }
    
    
}
