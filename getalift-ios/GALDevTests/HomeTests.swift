//
//  HomeTests.swift
//  GALDevTests
//
//  Created by Loan Aubergeon on 05/10/2017.
//  Copyright © 2017 Loan Aubergeon. All rights reserved.
//

import XCTest
@testable import GALDev

class HomeTests: XCTestCase {
    var controller: Home!
    
    override func setUp() {
        super.setUp()
        
        let storyboard = UIStoryboard(name: "Main", bundle: Bundle.main)
        controller = storyboard.instantiateInitialViewController() as! Home
    }
    
    override func tearDown() {
        super.tearDown()
        controller = nil
    }
    
    func testUsernameField() {
        let _ = controller.view
        controller.usernameField.text = "Bonjour"
        XCTAssertEqual("Bonjour", controller.usernameField.text)
    }
    
}


