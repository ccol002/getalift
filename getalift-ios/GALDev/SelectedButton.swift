//
//  SelectedButton.swift
//  GALDev
//
//  Created by Loan Aubergeon on 25/08/2017.
//  Copyright © 2017 Loan Aubergeon. All rights reserved.
//

import UIKit

class SelectedButton: UIButton {
    var alternateButton:Array<SelectedButton>?
    
    override func awakeFromNib() {
        self.layer.cornerRadius = 5
        self.layer.borderWidth = 2.0
        self.layer.masksToBounds = true
    }
    
    func unselectAlternateButtons(){
        if alternateButton != nil {
            self.isSelected = true
            
            for aButton:SelectedButton in alternateButton! {
                aButton.isSelected = false
            }
        }else{
            toggleButton()
        }
    }
    
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        unselectAlternateButtons()
        super.touchesBegan(touches, with: event)
    }
    
    func toggleButton(){
        self.isSelected = !isSelected
    }
    
    override var isSelected: Bool {
        didSet {
            if isSelected {
                self.layer.borderColor = (UIColor.black.cgColor)
            } else {
                self.layer.borderColor = (UIColor.gray.cgColor)
            }
        }
    }
}
