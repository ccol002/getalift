//
//  Comment.swift
//  GALDev
//
//  Created by Charly Joncheray on 26/09/2018.
//  Copyright © 2018 Loan Aubergeon. All rights reserved.
//

import Foundation

struct Comment {
    
    var target: Int
    
    var nameOfAuthor: String
    
    var comment: String
    
    var date: String
    
    init(target: Int, nameOfAuthor: String, comment: String, date: String) {
        self.target = target
        self.nameOfAuthor = nameOfAuthor
        self.comment = comment
        self.date = date
    }
    
}
