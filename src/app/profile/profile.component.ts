
// import { Component, OnInit } from '@angular/core';
// import { Post } from '../model/post';
// import { PostService } from '../Services/post.service';
// import { UserService } from '../Services/user.service';
// import { AuthService } from '../Services/auth.service';
// import { Router } from '@angular/router';
// import { UserProfileDTO } from '../model/UserProfile';

// @Component({
//   selector: 'app-profile',
//   templateUrl: './profile.component.html',
//   styleUrls: ['./profile.component.css']
// })
// export class ProfileComponent implements OnInit {

//   profile: UserProfileDTO = {
//     username: '',
//     email: '',
//     profilePicUrl: '',
//     bio: ''
//   };

//   myPosts: Post[] = [];
//   newPost: Post = { content: '', imageUrl: '', likes: 0 };

// profilePicUrl: string = '';
//   selectedProfilePic!: File;
//   selectedPostImage!: File;
//   username: string = '';

//   bio: string = '';
//   editMode: boolean = false;
//   user: any;

//   timeStamp: number = new Date().getTime();  // 🔥 used to force image refresh

//   constructor(
//     private postService: PostService,
//     private userService: UserService,
//     private auth: AuthService,
//     private router: Router
//   ) {}

//   ngOnInit(): void {
//     this.loadMyPosts();
//     this.loadProfilePics();
//     this.loadBio();
//     this.loadProfile();
//   }

//   // ✅ Load profile with timestamp for image cache-busting
//   loadProfile() {
//     this.userService.getCurrentUser().subscribe({
//       next: (user: UserProfileDTO) => {
//         this.profile = user;
//         this.bio = user.bio;
//         this.timeStamp = new Date().getTime(); 
//          // 🔁 Update timestamp after change
//          this.username = user.username;
//         this.profilePicUrl = user.profilePicUrl;
//       },
//       error: (err) => {
//         console.error('Error loading user profile:', err);
//       }
//     });
//   }

//   loadMyPosts() {
//     const email = this.auth.getUserEmail();
//     this.postService.getMyPosts(email).subscribe(posts => {
//       this.myPosts = posts.map(p => ({ ...p, newComment: '', liked: false }));
//     });
//   }

//   onPostImageSelected(event: any) {
//     this.selectedPostImage = event.target.files[0];
//   }

//   createPost() {
//     if (!this.newPost.content || !this.newPost.content.trim()) {
//       alert("Post content cannot be empty!");
//       return;
//     }

//     if (this.selectedPostImage) {
//       const formData = new FormData();
//       formData.append('image', this.selectedPostImage);

//       this.postService.uploadPostImage(formData).subscribe({
//         next: (res) => {
//           const imageUrl = res.imageUrl;
//           this.newPost.imageUrl = imageUrl;

//           this.postService.createPost(this.newPost).subscribe(() => {
//             this.resetPostForm();
//             this.router.navigate(['/dashboard']);
//           });
//         },
//         error: () => {
//           alert("Failed to upload post image.");
//         }
//       });

//     } else {
//       this.postService.createPost(this.newPost).subscribe(() => {
//         this.resetPostForm();
//         this.router.navigate(['/dashboard']);
//       });
//     }
//   }

//   deletePost(id: number) {
//     this.postService.deletePost(id).subscribe({
//       next: () => this.loadMyPosts(),
//       error: (err) => {
//         console.error("Failed to delete post", err);
//         alert("Failed to delete post.");
//       }
//     });
//   }

//   onProfilePicSelected(event: any) {
//     this.selectedProfilePic = event.target.files[0];
//   }

//   uploadProfilePic() {
//     if (!this.selectedProfilePic) {
//       alert("Please select a profile picture!");
//       return;
//     }

//     const formData = new FormData();
//     formData.append('image', this.selectedProfilePic);

//     this.userService.uploadProfilePic(formData).subscribe({
//       next: () => {
//         this.loadProfile();
//          // 🔁 updates profile with new image and refreshes timestamp
//            this.timeStamp = Date.now();
//         alert("Uploaded Successfully");
//       },
//       error: (err) => {
//         console.error(err);
//         alert("Failed to upload profile picture.");
//       }
//     });
//   }

//   loadProfilePics() {
//     this.userService.getAllProfilePics().subscribe({
//       next: (res: any) => {
//         this.profilePicUrl = res.map((img: any) => img.imageUrl);
//       },
//       error: (err: any) => console.error("Failed to load profile pictures", err)
//     });
//   }

//   loadBio() {
//     this.userService.getBio().subscribe({
//       next: (res) => this.bio = res,
//       error: () => this.bio = ''
//     });
//   }

//   saveBio() {
//     this.userService.updateBio(this.bio).subscribe({
//       next: () => {
//         this.profile.bio = this.bio;
//         this.editMode = false;
//       },
//       error: (err) => {
//         console.error("Error updating bio:", err);
//         alert("Failed to update bio.");
//       }
//     });
//   }

//   cancelEdit() {
//     this.editMode = false;
//     this.loadBio();
//   }

//   enableEdit() {
//     this.editMode = true;
//     this.bio = this.profile.bio;
//   }

//   private resetPostForm() {
//     this.newPost = { content: '', imageUrl: '', likes: 0 };
//     this.selectedPostImage = undefined!;
//     this.loadMyPosts();
//   }
// }

import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Post } from '../model/post';
import { PostService } from '../Services/post.service';
import { UserService } from '../Services/user.service';
import { AuthService } from '../Services/auth.service';
import { Router } from '@angular/router';
import { UserProfileDTO } from '../model/UserProfile';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  profile: UserProfileDTO = {
    username: '',
    email: '',
    profilePicUrl: '',
    bio: ''
  };

  myPosts: Post[] = [];
  newPost: Post = { content: '', imageUrl: '', likes: 0 };

  selectedProfilePic!: File;
  selectedPostImage!: File;

  bio: string = '';
  editMode: boolean = false;

  timeStamp: number = new Date().getTime(); // 🔁 refresh image on upload

  constructor(
    private postService: PostService,
    private userService: UserService,
    private auth: AuthService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadMyPosts();
    this.loadProfile();
    this.loadBio();
  }

  loadProfile() {
    this.userService.getCurrentUser().subscribe({
      next: (user: UserProfileDTO) => {
        this.profile = user;
        this.bio = user.bio;
        this.timeStamp = Date.now(); // refresh image
      },
      error: (err) => {
        console.error('Error loading user profile:', err);
      }
    });
  }

  loadMyPosts() {
    const email = this.auth.getUserEmail();
    this.postService.getMyPosts(email).subscribe(posts => {
      this.myPosts = posts.map(p => ({ ...p, newComment: '', liked: false }));
    });
  }

  onPostImageSelected(event: any) {
    this.selectedPostImage = event.target.files[0];
  }

  // createPost() {
  //   if (!this.newPost.content || !this.newPost.content.trim()) {
  //     alert("Post content cannot be empty!");
  //     return;
  //   }

  //   if (this.selectedPostImage) {
  //     const formData = new FormData();
  //     formData.append('image', this.selectedPostImage);

  //     this.postService.uploadPostImage(formData).subscribe({
  //       next: (res) => {
  //         this.newPost.imageUrl = res.imageUrl;
  //         this.postService.createPost(this.newPost).subscribe(() => {
  //           this.resetPostForm();
  //           this.loadMyPosts();
  //         });
  //       },
  //       error: () => alert("Failed to upload post image.")
  //     });

  //   } else {
  //     this.postService.createPost(this.newPost).subscribe(() => {
  //       this.resetPostForm();
  //       this.loadMyPosts();
  //     });
  //   }
  // }
//   createPost() {
//   if (!this.newPost.content || !this.newPost.content.trim()) {
//     alert("Post content cannot be empty!");
//     return;
//   }

//   if (this.selectedPostImage) {
//     const formData = new FormData();
//     formData.append('image', this.selectedPostImage);

//     this.postService.uploadPostImage(formData).subscribe({
//       next: (res) => {
//         this.newPost.imageUrl = res.imageUrl;

//         this.postService.createPost(this.newPost).subscribe((createdPost) => {
//           this.myPosts.unshift(createdPost); // Add to top of the feed
//           this.resetPostForm();
//           this.router.navigate(['/dashboard']);
//         });
//       },
//       error: () => alert("Failed to upload post image.")
//     });

//   } else {
//     this.postService.createPost(this.newPost).subscribe((createdPost) => {
//       this.myPosts.unshift(createdPost); // Add to top of the feed
//       this.resetPostForm();
//     });
//   }
// }
createPost() {
  if (!this.newPost.content || !this.newPost.content.trim()) {
    alert("Post content cannot be empty!");
    return;
  }

  if (this.selectedPostImage) {
    const formData = new FormData();
    formData.append('image', this.selectedPostImage);

    this.postService.uploadPostImage(formData).subscribe({
      next: (res) => {
        this.newPost.imageUrl = res.imageUrl;

        this.postService.createPost(this.newPost).subscribe((createdPost) => {
          this.myPosts.unshift(createdPost); // Add to top of the feed
          this.resetPostForm();
           this.cdr.detectChanges();
          this.router.navigate(['/dashboard']); // ✅ Move inside here
        });
      },
      error: () => alert("Failed to upload post image.")
    });

  } else {
    this.postService.createPost(this.newPost).subscribe((createdPost) => {
      this.myPosts.unshift(createdPost); // Add to top of the feed
      this.resetPostForm();
      this.router.navigate(['/dashboard']); // ✅ Also redirect for text-only post
    });
  }
}


  deletePost(id: number) {
    this.postService.deletePost(id).subscribe({
      next: () => this.loadMyPosts(),
      error: (err) => {
        console.error("Failed to delete post", err);
        alert("Failed to delete post.");
      }
    });
  }

  onProfilePicSelected(event: any) {
    this.selectedProfilePic = event.target.files[0];
  }

  uploadProfilePic() {
    if (!this.selectedProfilePic) {
      alert("Please select a profile picture!");
      return;
    }

    const formData = new FormData();
    formData.append('image', this.selectedProfilePic);

    this.userService.uploadProfilePic(formData).subscribe({
      next: () => {
        this.loadProfile();
        this.timeStamp = Date.now(); // 🟢 refresh profile picture
        alert("Uploaded Successfully");
      },
      error: (err) => {
        console.error(err);
        alert("Failed to upload profile picture.");
      }
    });
  }

  loadBio() {
    this.userService.getBio().subscribe({
      next: (res) => this.bio = res,
      error: () => this.bio = ''
    });
  }

  saveBio() {
    this.userService.updateBio(this.bio).subscribe({
      next: () => {
        this.profile.bio = this.bio;
        this.editMode = false;
      },
      error: (err) => {
        console.error("Error updating bio:", err);
        alert("Failed to update bio.");
      }
    });
  }

  cancelEdit() {
    this.editMode = false;
    this.loadBio();
  }

  enableEdit() {
    this.editMode = true;
    this.bio = this.profile.bio;
  }

  private resetPostForm() {
    this.newPost = { content: '', imageUrl: '', likes: 0 };
    this.selectedPostImage = undefined!;
  }
}

