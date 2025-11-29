import { Component, OnInit } from '@angular/core';
import { UserService } from '../Services/user.service';

@Component({
  selector: 'app-user-search',
  templateUrl: './user-search.component.html',
  styleUrls: ['./user-search.component.css']
})
export class UserSearchComponent implements OnInit {

  keyword: string = '';
  results: any[] = [];
  recentSearches: any[] = [];

  constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.loadRecentSearches();
  }

  search() {
    if (this.keyword.trim()) {
      this.userService.searchUsers(this.keyword).subscribe({
        next: (res) => {
          this.results = res;

          // record the search for recent list
          if (res.length > 0) {
            this.userService.recordSearch(res[0].email).subscribe();
          }

        },
        error: (err) => {
          console.error(err);
          alert("Failed to search users.");
        }
      });
    } else {
      this.results = [];
    }
  }

  loadRecentSearches() {
    this.userService.getRecentSearches().subscribe({
      next: (res) => {
        this.recentSearches = res;
      },
      error: (err) => {
        console.error(err);
      }
    });
  }
}



