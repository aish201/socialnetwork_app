import { Component, OnInit, NgZone } from '@angular/core';
import { Post } from 'src/app/model/post';
import { Comment } from 'src/app/model/comment';
import { PostService } from 'src/app/Services/post.service';
import { AuthService } from '../Services/auth.service';
import { NotificationService } from '../Services/notification.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  posts: Post[] = [];
  username: string = '';
  email: string = '';

  constructor(
    private postService: PostService,
    private auth: AuthService,
    private notificationService: NotificationService,
    private ngZone: NgZone
  ) {}

  ngOnInit(): void {
    this.username = this.auth.getUsername();
    this.email = this.auth.getUserEmail();  // ✅ fixed method name
    this.loadPosts();

    // ✅ Initialize websocket notifications
    this.notificationService.init();  // use 'init' instead of 'connect'
    this.notificationService.getNotifications().subscribe(notifList => {
      this.ngZone.run(() => {
        console.log('New notifications:', notifList);
        // optional: refresh posts to reflect likes/comments
        // this.refreshPosts();
      });
    });
  }

  loadPosts() {
    this.postService.getAllPosts().subscribe({
      next: res => {
        this.posts = res.map(p => ({
          ...p,
          newComment: '',
          showEmojiPicker: false,
          liked: p.liked || false,   // ensure liked flag exists
          comments: p.comments || []
        }));
      },
      error: err => console.error("Failed to fetch posts:", err)
    });
  }

  likePost(post: Post) {
    if (!post.id) return;

    if (post.liked) {
      this.postService.unlikePost(post.id).subscribe({
        next: () => {
          post.liked = false;
          post.likes = (post.likes || 1) - 1;
        },
        error: () => alert('Failed to unlike post')
      });
    } else {
      this.postService.likePost(post.id).subscribe({
        next: () => {
          post.liked = true;
          post.likes = (post.likes || 0) + 1;
        },
        error: () => alert('Failed to like post')
      });
    }
  }
  

  toggleEmojiPicker(post: Post) {
    post.showEmojiPicker = !post.showEmojiPicker;
  }

  addEmoji(post: Post, event: any) {
    post.newComment = (post.newComment || '') + event.emoji.native;
  }

  addComment(post: Post) {
    if (!post.id || !post.newComment?.trim()) return;

    const comment: Comment = { text: post.newComment.trim() };
    this.postService.addComment(post.id, comment).subscribe({
      next: (newComment) => {
        post.comments?.unshift(newComment);
        post.newComment = '';
        post.showEmojiPicker = false;
      },
      error: () => alert("Failed to add comment")
    });
  }

  refreshPosts() {
    this.loadPosts();  // manually reload posts if needed
  }
}
