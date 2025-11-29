import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Post } from '../model/post';
import { PostService } from '../Services/post.service';
import { UserService } from '../Services/user.service';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent implements OnInit {

  user: any = null;
  posts: Post[] = [];
  profilePicUrl: string = 'assets/default_profile.png';

  constructor(
    private route: ActivatedRoute,
    private userService: UserService,
    private postService: PostService
  ) {}

  ngOnInit(): void {
    const email = this.route.snapshot.paramMap.get('email');
    if (email) {
      this.loadUser(email);
      this.loadPosts(email);
      this.loadProfilePic(email);
    }
  }

  loadUser(email: string) {
    this.userService.getUserByEmail(email).subscribe({
      next: (res) => {
        this.user = res;
      },
      error: (err) => {
        console.error("Failed to load user details", err);
      }
    });
  }

  loadPosts(email: string) {
    this.postService.getMyPosts(email).subscribe({
      next: (res) => {
        this.posts = res;
      },
      error: (err) => {
        console.error("Failed to load posts", err);
      }
    });
  }

  loadProfilePic(email: string) {
    this.userService.getProfilePicsByEmail(email).subscribe({
      next: (res: any) => {
        if (res.length) {
          this.profilePicUrl = res[res.length - 1].imageUrl;
        }
      },
      error: (err) => {
        console.error("Failed to load profile pic", err);
      }
    });
  }
}



