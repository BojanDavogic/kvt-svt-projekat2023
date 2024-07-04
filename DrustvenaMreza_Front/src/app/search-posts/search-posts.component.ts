import { Component, EventEmitter, OnDestroy, Output } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { SearchService } from '../services/search.service';
import { Subject } from 'rxjs';
import { takeUntil, distinctUntilChanged } from 'rxjs/operators';

@Component({
  selector: 'app-search-posts',
  templateUrl: './search-posts.component.html',
  styleUrls: ['./search-posts.component.css']
})
export class SearchPostsComponent implements OnDestroy {
  searchForm: FormGroup;
  @Output() searchResults = new EventEmitter<any[]>();
  private destroy$ = new Subject<void>();

  constructor(private fb: FormBuilder, private searchService: SearchService) {
    this.searchForm = this.fb.group({
      type: ['simple'],
      simpleField: ['title'],
      title: [''],
      text: [''],
      pdfContent: [''],
      comments: [''],
      likesMin: [''],
      likesMax: [''],
      commentMin:[''],
      commentMax:[''],
      page: ['0'],
      size: ['10']
    });

    this.initValueChanges();
  }

  initValueChanges() {
    this.searchForm.get('type')?.valueChanges.pipe(
      takeUntil(this.destroy$)
    ).subscribe(type => {
      this.clearFields();
      if (type === 'simple') {
        this.clearAdvancedFields();
      }
    });

    this.searchForm.get('simpleField')?.valueChanges.pipe(
      takeUntil(this.destroy$),
      distinctUntilChanged()
    ).subscribe(field => {
      this.clearFields();
    });
  }

  clearFields() {
    const fields = ['title', 'text', 'pdfContent', 'comments', 'likesMin', 'likesMax', 'commentMin', 'commentMax'];
    fields.forEach(field => this.searchForm.get(field)?.setValue(''));
  }

  clearAdvancedFields() {
    const fields = ['title', 'text', 'pdfContent', 'comments', 'likesMin', 'likesMax', 'commentMin', 'commentMax'];
    fields.forEach(field => this.searchForm.get(field)?.disable());
  }

  enableAllFields() {
    const fields = ['title', 'text', 'pdfContent', 'comments', 'likesMin', 'likesMax', 'commentMin', 'commentMax'];
    fields.forEach(field => this.searchForm.get(field)?.enable());
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }

  onSearch() {
    const searchParams = this.searchForm.value;
    if (searchParams.type === 'simple') {
      const simpleField = searchParams.simpleField;
      let params: any = {};
  
      if (simpleField === 'likes') {
        const minLikes = searchParams.likesMin;
        const maxLikes = searchParams.likesMax;
  
        if (minLikes && maxLikes) {
          params = {
            query: `${minLikes}-${maxLikes}`
          };
        } else if (minLikes) {
          params = {
            query: `${minLikes}`
          };
        } else if (maxLikes) {
          params = {
            query: `${maxLikes}`
          };
        } else {
          params = {
            query: ''
          };
        }
      } else {
        params = {
          query: searchParams[simpleField]
        };
      }
  
      this.searchService.simpleSearchPosts(params).subscribe((data: any) => {
        const results = data.content || data;
        this.searchResults.emit(results);
      });
    } else {
      this.searchService.advancedSearchPosts(searchParams).subscribe((data: any) => {
        const results = data.content || data;
        this.searchResults.emit(results);
      });
    }
  }
}
