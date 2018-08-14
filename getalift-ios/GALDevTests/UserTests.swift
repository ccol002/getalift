//
//  UserTests.swift
//  GALDevTests
//
//  Created by Loan Aubergeon on 06/10/2017.
//  Copyright © 2017 Loan Aubergeon. All rights reserved.
//

import XCTest
@testable import GALDev

class UserTests: XCTestCase {
    
    override func setUp() {
        super.setUp()
        // Put setup code here. This method is called before the invocation of each test method in the class.
    }
    
    override func tearDown() {
        // Put teardown code here. This method is called after the invocation of each test method in the class.
        super.tearDown()
    }
    
    
    func testUserInitNil(){
        let user = User.init()
        
        XCTAssertNil(user.id)
        XCTAssertNil(user.username)
        XCTAssertNil(user.name)
        XCTAssertNil(user.surname)
        XCTAssertNil(user.mobileNumber)
        XCTAssertNil(user.email)
    }
    
    func testUserInitFull(){
        let user = User.init(id: 1, username: "auberglo", name: "Loan", surname: "Aubergeon", email: "laubergeon@gmail.com", mobileNumber: "33626255780")
        
        XCTAssertEqual(1, user.id)
        XCTAssertEqual("auberglo", user.username)
        XCTAssertEqual("Loan", user.name)
        XCTAssertEqual("Aubergeon", user.surname)
        XCTAssertEqual("33626255780", user.mobileNumber)
        XCTAssertEqual("laubergeon@gmail.com", user.email)
    }
    
}


