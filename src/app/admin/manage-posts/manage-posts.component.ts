import { Component, OnInit } from '@angular/core';
import { AdminService } from 'src/app/Services/admin.service';

interface Post {
  id: number;
  content: string;
  imageUrl?: string;
  username: string;
  createdAt: string;
  likes: number;
}

@Component({
  selector: 'app-manage-posts',
  templateUrl: './manage-posts.component.html',
  styleUrls: ['./manage-posts.component.css']
})
export class ManagePostsComponent implements OnInit {

  posts: Post[] = [];

  constructor(private adminService: AdminService) {}

  ngOnInit(): void {
    this.loadPosts();
  }

  loadPosts() {
    this.adminService.getAllPosts().subscribe({
      next: (res: Post[]) => this.posts = res,
      error: err => console.error('Error fetching posts', err)
    });
  }

  deletePost(id: number) {
    if (confirm('Are you sure you want to delete this post?')) {
      this.adminService.deletePost(id).subscribe({
        next: () => this.loadPosts(),
        error: err => console.error('Error deleting post', err)
      });
    }
  }
}
