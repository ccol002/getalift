//
//  CalculationForMapDisplayTests.swift
//  GALDevTests
//
//  Created by Loan Aubergeon on 06/10/2017.
//  Copyright © 2017 Loan Aubergeon. All rights reserved.
//

import XCTest
@testable import GALDev

class CalculationForMapDisplayTests: XCTestCase {
    
    override func setUp() {
        super.setUp()
        // Put setup code here. This method is called before the invocation of each test method in the class.
    }
    
    override func tearDown() {
        // Put teardown code here. This method is called after the invocation of each test method in the class.
        super.tearDown()
    }
    
    //   Tests with A on (0,0)
    //
    //                      ^
    //             B7                 | B8          B1
    //                 X              X           X
    //                                |
    //                                |
    //               B6               |A            B2
    //           ------X--------------O-----------X-------->
    //                                |
    //                                |
    //                                |
    //                 X              X           X
    //               B5               | B4          B3
    //
    
    
    func testCalculCenter_1_B0(){
        let calculationForMapDisplay = CalculationForMapDisplay()
        calculationForMapDisplay.centerCalcul(xA: 0, yA: 0, xB: 0, yB: 0)
        XCTAssertEqual(0, calculationForMapDisplay.xCenter)
        XCTAssertEqual(0, calculationForMapDisplay.yCenter)
    }
    
    func testCalculCenter_1_B1(){
        let calculationForMapDisplay = CalculationForMapDisplay()
        calculationForMapDisplay.centerCalcul(xA: 0, yA: 0, xB: 5, yB: 5)
        XCTAssertEqual(2.5, calculationForMapDisplay.xCenter)
        XCTAssertEqual(2.5, calculationForMapDisplay.yCenter)
    }
    
    func testCalculCenter_1_B2(){
        let calculationForMapDisplay = CalculationForMapDisplay()
        calculationForMapDisplay.centerCalcul(xA: 0, yA: 0, xB: 5, yB: 0)
        XCTAssertEqual(2.5, calculationForMapDisplay.xCenter)
        XCTAssertEqual(0, calculationForMapDisplay.yCenter)
    }
    func testCalculCenter_1_B3(){
        let calculationForMapDisplay = CalculationForMapDisplay()
        calculationForMapDisplay.centerCalcul(xA: 0, yA: 0, xB: 5, yB: -5)
        XCTAssertEqual(2.5, calculationForMapDisplay.xCenter)
        XCTAssertEqual(-2.5, calculationForMapDisplay.yCenter)
    }
    func testCalculCenter_1_B4(){
        let calculationForMapDisplay = CalculationForMapDisplay()
        calculationForMapDisplay.centerCalcul(xA : 0, yA : 0, xB : 0, yB : -5)
        XCTAssertEqual(0, calculationForMapDisplay.xCenter)
        XCTAssertEqual(-2.5, calculationForMapDisplay.yCenter)
    }
    func testCalculCenter_1_B5(){
        let calculationForMapDisplay = CalculationForMapDisplay()
        calculationForMapDisplay.centerCalcul(xA : 0, yA : 0, xB : -5, yB : -5)
        XCTAssertEqual(-2.5, calculationForMapDisplay.xCenter)
        XCTAssertEqual(-2.5, calculationForMapDisplay.yCenter)
    }
    func testCalculCenter_1_B6(){
        let calculationForMapDisplay = CalculationForMapDisplay()
        calculationForMapDisplay.centerCalcul(xA : 0, yA : 0, xB : -5, yB : 0)
        XCTAssertEqual(-2.5, calculationForMapDisplay.xCenter)
        XCTAssertEqual(0, calculationForMapDisplay.yCenter)
    }
    func testCalculCenter_1_B7(){
        let calculationForMapDisplay = CalculationForMapDisplay()
        calculationForMapDisplay.centerCalcul(xA: 0, yA: 0, xB: -5, yB: 5)
        XCTAssertEqual(-2.5, calculationForMapDisplay.xCenter)
        XCTAssertEqual(2.5, calculationForMapDisplay.yCenter)
    }
    func testCalculCenter_1_B8(){
        let calculationForMapDisplay = CalculationForMapDisplay()
        calculationForMapDisplay.centerCalcul(xA: 0, yA: 0, xB: 0, yB: 5)
        XCTAssertEqual(0, calculationForMapDisplay.xCenter)
        XCTAssertEqual(2.5, calculationForMapDisplay.yCenter)
    }
    
    // Tests with A on (1,1)
    //
    //                      ^
    //   B7                 | B8          B1
    //       X              X           X
    //                      |
    //                      |  A
    //     B6               |X            B2
    // ------X--------------------------X-------->
    //                      |
    //                      |
    //                      |
    //       X              X           X
    //     B5               | B4          B3
    //
    
    
    func testCalculCenter_2_B0(){
        let calculationForMapDisplay = CalculationForMapDisplay()
        calculationForMapDisplay.centerCalcul(xA: 1, yA: 1, xB: 0, yB: 0)
        XCTAssertEqual(0.5, calculationForMapDisplay.xCenter)
        XCTAssertEqual(0.5, calculationForMapDisplay.yCenter)
    }
    
    func testCalculCenter_2_B1(){
        let calculationForMapDisplay = CalculationForMapDisplay()
        calculationForMapDisplay.centerCalcul(xA: 1, yA: 1, xB: 5, yB: 5)
        XCTAssertEqual(3, calculationForMapDisplay.xCenter)
        XCTAssertEqual(3, calculationForMapDisplay.yCenter)
    }
    
    func testCalculCenter_2_B2(){
        let calculationForMapDisplay = CalculationForMapDisplay()
        calculationForMapDisplay.centerCalcul(xA: 1, yA: 1, xB: 5, yB: 0)
        XCTAssertEqual(3, calculationForMapDisplay.xCenter)
        XCTAssertEqual(0.5, calculationForMapDisplay.yCenter)
    }
    func testCalculCenter_2_B3(){
        let calculationForMapDisplay = CalculationForMapDisplay()
        calculationForMapDisplay.centerCalcul(xA: 1, yA: 1, xB: 5, yB: -5)
        XCTAssertEqual(3, calculationForMapDisplay.xCenter)
        XCTAssertEqual(-2, calculationForMapDisplay.yCenter)
    }
    func testCalculCenter_2_B4(){
        let calculationForMapDisplay = CalculationForMapDisplay()
        calculationForMapDisplay.centerCalcul(xA : 1, yA : 1, xB : 0, yB : -5)
        XCTAssertEqual(0.5, calculationForMapDisplay.xCenter)
        XCTAssertEqual(-2, calculationForMapDisplay.yCenter)
    }
    func testCalculCenter_2_B5(){
        let calculationForMapDisplay = CalculationForMapDisplay()
        calculationForMapDisplay.centerCalcul(xA : 1, yA : 1, xB : -5, yB : -5)
        XCTAssertEqual(-2, calculationForMapDisplay.xCenter)
        XCTAssertEqual(-2, calculationForMapDisplay.yCenter)
    }
    func testCalculCenter_2_B6(){
        let calculationForMapDisplay = CalculationForMapDisplay()
        calculationForMapDisplay.centerCalcul(xA : 1, yA : 1, xB : -5, yB : 0)
        XCTAssertEqual(-2, calculationForMapDisplay.xCenter)
        XCTAssertEqual(0.5, calculationForMapDisplay.yCenter)
    }
    func testCalculCenter_2_B7(){
        let calculationForMapDisplay = CalculationForMapDisplay()
        calculationForMapDisplay.centerCalcul(xA: 1, yA: 1, xB: -5, yB: 5)
        XCTAssertEqual(-2, calculationForMapDisplay.xCenter)
        XCTAssertEqual(3, calculationForMapDisplay.yCenter)
    }
    func testCalculCenter_2_B8(){
        let calculationForMapDisplay = CalculationForMapDisplay()
        calculationForMapDisplay.centerCalcul(xA: 1, yA: 1, xB: 0, yB: 5)
        XCTAssertEqual(0.5, calculationForMapDisplay.xCenter)
        XCTAssertEqual(3, calculationForMapDisplay.yCenter)
    }
    
    // Tests with A on (1,-1)
    //
    //                      ^
    //   B7                 | B8          B1
    //       X              X           X
    //                      |
    //                      |
    //     B6               |             B2
    // ------X--------------------------X-------->
    //                      | X
    //                      |   A
    //                      |
    //       X              X           X
    //     B5               | B4          B3
    //
    
    
    func testCalculCenter_3_B0(){
        let calculationForMapDisplay = CalculationForMapDisplay()
        calculationForMapDisplay.centerCalcul(xA: 1, yA: -1, xB: 0, yB: 0)
        XCTAssertEqual(0.5, calculationForMapDisplay.xCenter)
        XCTAssertEqual(-0.5, calculationForMapDisplay.yCenter)
    }
    
    func testCalculCenter_3_B1(){
        let calculationForMapDisplay = CalculationForMapDisplay()
        calculationForMapDisplay.centerCalcul(xA: 1, yA: -1, xB: 5, yB: 5)
        XCTAssertEqual(3, calculationForMapDisplay.xCenter)
        XCTAssertEqual(2, calculationForMapDisplay.yCenter)
    }
    
    func testCalculCenter_3_B2(){
        let calculationForMapDisplay = CalculationForMapDisplay()
        calculationForMapDisplay.centerCalcul(xA: 1, yA: -1, xB: 5, yB: 0)
        XCTAssertEqual(3, calculationForMapDisplay.xCenter)
        XCTAssertEqual(-0.5, calculationForMapDisplay.yCenter)
    }
    func testCalculCenter_3_B3(){
        let calculationForMapDisplay = CalculationForMapDisplay()
        calculationForMapDisplay.centerCalcul(xA: 1, yA: -1, xB: 5, yB: -5)
        XCTAssertEqual(3, calculationForMapDisplay.xCenter)
        XCTAssertEqual(-3, calculationForMapDisplay.yCenter)
    }
    func testCalculCenter_3_B4(){
        let calculationForMapDisplay = CalculationForMapDisplay()
        calculationForMapDisplay.centerCalcul(xA : 1, yA : -1, xB : 0, yB : -5)
        XCTAssertEqual(0.5, calculationForMapDisplay.xCenter)
        XCTAssertEqual(-3, calculationForMapDisplay.yCenter)
    }
    func testCalculCenter_3_B5(){
        let calculationForMapDisplay = CalculationForMapDisplay()
        calculationForMapDisplay.centerCalcul(xA : 1, yA : -1, xB : -5, yB : -5)
        XCTAssertEqual(-2, calculationForMapDisplay.xCenter)
        XCTAssertEqual(-3, calculationForMapDisplay.yCenter)
    }
    func testCalculCenter_3_B6(){
        let calculationForMapDisplay = CalculationForMapDisplay()
        calculationForMapDisplay.centerCalcul(xA : 1, yA : -1, xB : -5, yB : 0)
        XCTAssertEqual(-2, calculationForMapDisplay.xCenter)
        XCTAssertEqual(-0.5, calculationForMapDisplay.yCenter)
    }
    func testCalculCenter_3_B7(){
        let calculationForMapDisplay = CalculationForMapDisplay()
        calculationForMapDisplay.centerCalcul(xA: 1, yA: -1, xB: -5, yB: 5)
        XCTAssertEqual(-2, calculationForMapDisplay.xCenter)
        XCTAssertEqual(2, calculationForMapDisplay.yCenter)
    }
    func testCalculCenter_3_B8(){
        let calculationForMapDisplay = CalculationForMapDisplay()
        calculationForMapDisplay.centerCalcul(xA: 1, yA: -1, xB: 0, yB: 5)
        XCTAssertEqual(0.5, calculationForMapDisplay.xCenter)
        XCTAssertEqual(2, calculationForMapDisplay.yCenter)
    }
    
    // Tests with A on (-1,-1)
    //
    //                      ^
    //   B7                 | B8          B1
    //       X              X           X
    //                      |
    //                      |
    //     B6               |             B2
    // ------X--------------------------X-------->
    //                    X |
    //                  A   |
    //                      |
    //       X              X           X
    //     B5               | B4          B3
    //
    
    
    func testCalculCenter_4_B0(){
        let calculationForMapDisplay = CalculationForMapDisplay()
        calculationForMapDisplay.centerCalcul(xA: -1, yA: -1, xB: 0, yB: 0)
        XCTAssertEqual(-0.5, calculationForMapDisplay.xCenter)
        XCTAssertEqual(-0.5, calculationForMapDisplay.yCenter)
    }
    
    func testCalculCenter_4_B1(){
        let calculationForMapDisplay = CalculationForMapDisplay()
        calculationForMapDisplay.centerCalcul(xA: -1, yA: -1, xB: 5, yB: 5)
        XCTAssertEqual(2, calculationForMapDisplay.xCenter)
        XCTAssertEqual(2, calculationForMapDisplay.yCenter)
    }
    
    func testCalculCenter_4_B2(){
        let calculationForMapDisplay = CalculationForMapDisplay()
        calculationForMapDisplay.centerCalcul(xA: -1, yA: -1, xB: 5, yB: 0)
        XCTAssertEqual(2, calculationForMapDisplay.xCenter)
        XCTAssertEqual(-0.5, calculationForMapDisplay.yCenter)
    }
    func testCalculCenter_4_B3(){
        let calculationForMapDisplay = CalculationForMapDisplay()
        calculationForMapDisplay.centerCalcul(xA: -1, yA: -1, xB: 5, yB: -5)
        XCTAssertEqual(2, calculationForMapDisplay.xCenter)
        XCTAssertEqual(-3, calculationForMapDisplay.yCenter)
    }
    func testCalculCenter_4_B4(){
        let calculationForMapDisplay = CalculationForMapDisplay()
        calculationForMapDisplay.centerCalcul(xA : -1, yA : -1, xB : 0, yB : -5)
        XCTAssertEqual(-0.5, calculationForMapDisplay.xCenter)
        XCTAssertEqual(-3, calculationForMapDisplay.yCenter)
    }
    func testCalculCenter_4_B5(){
        let calculationForMapDisplay = CalculationForMapDisplay()
        calculationForMapDisplay.centerCalcul(xA : -1, yA : -1, xB : -5, yB : -5)
        XCTAssertEqual(-3, calculationForMapDisplay.xCenter)
        XCTAssertEqual(-3, calculationForMapDisplay.yCenter)
    }
    func testCalculCenter_4_B6(){
        let calculationForMapDisplay = CalculationForMapDisplay()
        calculationForMapDisplay.centerCalcul(xA : -1, yA : -1, xB : -5, yB : 0)
        XCTAssertEqual(-3, calculationForMapDisplay.xCenter)
        XCTAssertEqual(-0.5, calculationForMapDisplay.yCenter)
    }
    func testCalculCenter_4_B7(){
        let calculationForMapDisplay = CalculationForMapDisplay()
        calculationForMapDisplay.centerCalcul(xA: -1, yA: -1, xB: -5, yB: 5)
        XCTAssertEqual(-3, calculationForMapDisplay.xCenter)
        XCTAssertEqual(2, calculationForMapDisplay.yCenter)
    }
    func testCalculCenter_4_B8(){
        let calculationForMapDisplay = CalculationForMapDisplay()
        calculationForMapDisplay.centerCalcul(xA: -1, yA: -1, xB: 0, yB: 5)
        XCTAssertEqual(-0.5, calculationForMapDisplay.xCenter)
        XCTAssertEqual(2, calculationForMapDisplay.yCenter)
    }
    
    // Tests with A on (-1,-1)
    //
    //                      ^
    //   B7                 | B8          B1
    //       X              X           X
    //                      |
    //                 A    |
    //     B6            X  |             B2
    // ------X--------------------------X-------->
    //                      |
    //                      |
    //                      |
    //       X              X           X
    //     B5               | B4          B3
    //
    
    
    func testCalculCenter_5_B0(){
        let calculationForMapDisplay = CalculationForMapDisplay()
        calculationForMapDisplay.centerCalcul(xA: -1, yA: 1, xB: 0, yB: 0)
        XCTAssertEqual(-0.5, calculationForMapDisplay.xCenter)
        XCTAssertEqual(0.5, calculationForMapDisplay.yCenter)
    }
    
    func testCalculCenter_5_B1(){
        let calculationForMapDisplay = CalculationForMapDisplay()
        calculationForMapDisplay.centerCalcul(xA: -1, yA: 1, xB: 5, yB: 5)
        XCTAssertEqual(2, calculationForMapDisplay.xCenter)
        XCTAssertEqual(3, calculationForMapDisplay.yCenter)
    }
    
    func testCalculCenter_5_B2(){
        let calculationForMapDisplay = CalculationForMapDisplay()
        calculationForMapDisplay.centerCalcul(xA: -1, yA: 1, xB: 5, yB: 0)
        XCTAssertEqual(2, calculationForMapDisplay.xCenter)
        XCTAssertEqual(0.5, calculationForMapDisplay.yCenter)
    }
    func testCalculCenter_5_B3(){
        let calculationForMapDisplay = CalculationForMapDisplay()
        calculationForMapDisplay.centerCalcul(xA: -1, yA: 1, xB: 5, yB: -5)
        XCTAssertEqual(2, calculationForMapDisplay.xCenter)
        XCTAssertEqual(-2, calculationForMapDisplay.yCenter)
    }
    func testCalculCenter_5_B4(){
        let calculationForMapDisplay = CalculationForMapDisplay()
        calculationForMapDisplay.centerCalcul(xA : -1, yA : 1, xB : 0, yB : -5)
        XCTAssertEqual(-0.5, calculationForMapDisplay.xCenter)
        XCTAssertEqual(-2, calculationForMapDisplay.yCenter)
    }
    func testCalculCenter_5_B5(){
        let calculationForMapDisplay = CalculationForMapDisplay()
        calculationForMapDisplay.centerCalcul(xA : -1, yA : 1, xB : -5, yB : -5)
        XCTAssertEqual(-3, calculationForMapDisplay.xCenter)
        XCTAssertEqual(-2, calculationForMapDisplay.yCenter)
    }
    func testCalculCenter_5_B6(){
        let calculationForMapDisplay = CalculationForMapDisplay()
        calculationForMapDisplay.centerCalcul(xA : -1, yA : 1, xB : -5, yB : 0)
        XCTAssertEqual(-3, calculationForMapDisplay.xCenter)
        XCTAssertEqual(0.5, calculationForMapDisplay.yCenter)
    }
    func testCalculCenter_5_B7(){
        let calculationForMapDisplay = CalculationForMapDisplay()
        calculationForMapDisplay.centerCalcul(xA: -1, yA: 1, xB: -5, yB: 5)
        XCTAssertEqual(-3, calculationForMapDisplay.xCenter)
        XCTAssertEqual(3, calculationForMapDisplay.yCenter)
    }
    func testCalculCenter_5_B8(){
        let calculationForMapDisplay = CalculationForMapDisplay()
        calculationForMapDisplay.centerCalcul(xA: -1, yA: 1, xB: 0, yB: 5)
        XCTAssertEqual(-0.5, calculationForMapDisplay.xCenter)
        XCTAssertEqual(3, calculationForMapDisplay.yCenter)
    }
}
