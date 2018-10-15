//
//  RidesTableViewCell.swift
//  GALDev
//
//  Created by Charly Joncheray on 26/09/2018.
//  Copyright © 2018 Loan Aubergeon. All rights reserved.
//

import UIKit

class RidesTableViewCell: UITableViewCell {

    // ---- OUTLETS ----
    
    @IBOutlet weak var origin: UILabel!
    @IBOutlet weak var destination: UILabel!
    @IBOutlet weak var driver: UILabel!
    @IBOutlet weak var date: UILabel!
    
    // ---- FUNCTIONS ----
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
