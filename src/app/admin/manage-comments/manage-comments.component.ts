import { Component, OnInit } from '@angular/core';
import { AdminService } from 'src/app/Services/admin.service';

@Component({
  selector: 'app-manage-comments',
  templateUrl: './manage-comments.component.html',
  styleUrls: ['./manage-comments.component.css']
})
export class ManageCommentsComponent implements OnInit {

  comments: any[] = [];

  constructor(private adminService: AdminService) {}

  ngOnInit(): void {
    this.loadComments();
  }

  // ✅ Load all comments
  loadComments() {
    this.adminService.getAllComments().subscribe(res => {
      this.comments = res;
    });
  }

  // ✅ Delete a comment
  deleteComment(id: number) {
    if (confirm('Are you sure you want to delete this comment?')) {
      this.adminService.deleteComment(id).subscribe(() => {
        this.loadComments();
      });
    }
  }

}

