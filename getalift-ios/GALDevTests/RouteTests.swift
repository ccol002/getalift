//
//  RouteTests.swift
//  GALDevTests
//
//  Created by Loan Aubergeon on 16/10/2017.
//  Copyright © 2017 Loan Aubergeon. All rights reserved.
//

import XCTest
@testable import GALDev

class RouteTests: XCTestCase {
    
    override func setUp() {
        super.setUp()
        // Put setup code here. This method is called before the invocation of each test method in the class.
    }
    
    override func tearDown() {
        // Put teardown code here. This method is called after the invocation of each test method in the class.
        super.tearDown()
    }
    
    
    func testUserInitNil(){
        let route = Route.init()
        
        XCTAssertNil(route.id)
        XCTAssertNil(route.overviewPolyline)
        XCTAssertNil(route.nameOfStartingPoint)
        XCTAssertNil(route.nameOfEndpoint)
        XCTAssertNil(route.latitudeOfStartigPoint)
        XCTAssertNil(route.longitudeOfStartingPoint)
        XCTAssertNil(route.latitudeOfEndPoint)
        XCTAssertNil(route.longitudeOfEndPoint)
        XCTAssertNil(route.driver)
        XCTAssertNil(route.date)
        XCTAssertNil(route.time)
        XCTAssertNil(route.recurrence)
        XCTAssertNil(route.distance)
        XCTAssertNil(route.duration)
    }
    
    func testUserInitFull(){
        let route = Route.init(id: 8, nameOfStartingPoint: "Msida", latitudeOfStartigPoint: 1, longitudeOfStartingPoint: 2, nameOfEndpoint: "Sliema", latitudeOfEndPoint: 4, longitudeOfEndPoint: 5, driver: 23, distance: "12km", duration: "8h")
        
        XCTAssertEqual(8, route.id)
        XCTAssertEqual("Msida", route.nameOfStartingPoint)
        XCTAssertEqual(1, route.latitudeOfStartigPoint)
        XCTAssertEqual(2, route.longitudeOfStartingPoint)
        XCTAssertEqual("Sliema", route.nameOfEndpoint)
        XCTAssertEqual(4, route.latitudeOfEndPoint)
        XCTAssertEqual(5, route.longitudeOfEndPoint)
        XCTAssertEqual(23, route.driver)
        XCTAssertEqual("12km", route.distance)
        XCTAssertEqual("8h", route.duration)
        
    }
    
}
