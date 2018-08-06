//
//  BoutonArrondi.swift
//  GALDev
//
//  Created by Loan Aubergeon on 24/08/2017.
//  Copyright © 2017 Loan Aubergeon. All rights reserved.
//

import UIKit

class BoutonArrondi : UIButton {
    
    //Intialiseur
    required init?(coder aDecoder: NSCoder) {
        //Initialiseur de la classe parente
        super.init(coder: aDecoder)
        
        layer.backgroundColor = UIColor(white: 0.6, alpha: 0.10).cgColor
        //Coins arrondis
        layer.cornerRadius = 5
        
        //Couleur de la bordure
        layer.borderColor = UIColor.black.cgColor
        
        //Epaisseur de la bordure
        layer.borderWidth = 1.5
        
        //Couleur du texte
        setTitleColor(UIColor.black, for: .normal)
        
        //Padding a gauche et a droite
        contentEdgeInsets = UIEdgeInsets(top: 5, left: 20, bottom: 5, right: 20)
        
    }
    
    
}
