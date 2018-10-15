//
//  Comments.swift
//  GALDev
//
//  Created by Charly Joncheray on 26/09/2018.
//  Copyright © 2018 Loan Aubergeon. All rights reserved.
//

import UIKit

class Comments: UITableViewController {

    // ---- VARIABLES ----
    var comments = [Comment]()
    
    var routes: [Route] = []
    
    // ---- TASKS ----
    var commentsTasks = CommentsTasks()
    
    // ---- FUNCTIONS ----
    
    override func viewDidLoad() {
        super.viewDidLoad()

        tableView.rowHeight = 150
        
        let driverId = self.routes[myIndex].driver

        commentsTasks.commentaries(targetId: driverId!) { (status, success) in
            if success {
                self.comments = self.commentsTasks.commentary
                DispatchQueue.main.async {
                    self.tableView.reloadData()
                }
            }
        }
        
    }


    // MARK: - Table view data source

    override func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }

    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return comments.count
    }

    
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cellIdentifier = "CommentaryTableViewCell"
        
        guard let cell = tableView.dequeueReusableCell(withIdentifier: cellIdentifier, for: indexPath) as? CommentaryTableViewCell else {
            fatalError("Erreur de conversion")
        }

        let comment = comments[indexPath.row]

        cell.author.text = comment.nameOfAuthor
        cell.date.text = comment.date
        cell.comment.text = comment.comment
        
        return cell
    }
    

}
