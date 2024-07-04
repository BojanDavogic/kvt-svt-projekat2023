import { Component, EventEmitter, Output, OnDestroy } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { GroupService } from '../services/group.service';
import { Group } from '../model/group.model';
import { Subject } from 'rxjs';
import { takeUntil, distinctUntilChanged } from 'rxjs/operators';
import { SearchService } from '../services/search.service';

@Component({
  selector: 'app-search-group',
  templateUrl: './search-group.component.html',
  styleUrls: ['./search-group.component.css']
})
export class SearchGroupComponent implements OnDestroy {
  @Output() searchResults = new EventEmitter<Group[]>();
  searchForm: FormGroup;
  private destroy$ = new Subject<void>();

  constructor(private fb: FormBuilder, private groupService: GroupService, private searchService: SearchService) {
    this.searchForm = this.fb.group({
      type: ['simple'],
      simpleField: ['name'],
      name: [''],
      description: [''],
      pdfContent: [''],
      postsMin: [''],
      postsMax: [''],
      avgLikesMin: [''],
      avgLikesMax: [''],
      rules: [''],
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
      } else {
        this.enableAllFields();
      }
    });

    this.searchForm.get('simpleField')?.valueChanges.pipe(
      takeUntil(this.destroy$),
      distinctUntilChanged()
    ).subscribe(() => {
      this.clearFields();
    });
  }

  clearFields() {
    const fields = ['name', 'description', 'pdfContent', 'postsMin', 'postsMax', 'avgLikesMin', 'avgLikesMax', 'rules'];
    fields.forEach(field => this.searchForm.get(field)?.setValue(''));
  }

  clearAdvancedFields() {
    const fields = ['name', 'description', 'pdfContent', 'postsMin', 'postsMax', 'avgLikesMin', 'avgLikesMax', 'rules'];
    fields.forEach(field => this.searchForm.get(field)?.disable());
  }

  enableAllFields() {
    const fields = ['name', 'description', 'pdfContent', 'postsMin', 'postsMax', 'avgLikesMin', 'avgLikesMax', 'rules'];
    fields.forEach(field => this.searchForm.get(field)?.enable());
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }

  onSearch() {
    const searchParams = this.searchForm.value;
    if (searchParams.type === 'simple') {
      let params: any = {};
  
      if (searchParams.simpleField === 'numberOfPosts') {
        const minPosts = searchParams.postsMin;
        const maxPosts = searchParams.postsMax;
  
        if (minPosts && maxPosts) {
          params = {
            query: `${minPosts}-${maxPosts}`
          };
        } else if (minPosts) {
          params = {
            query: `${minPosts}`
          };
        } else if (maxPosts) {
          params = {
            query: `${maxPosts}`
          };
        } else {
          params = {
            query: ''
          };
        }
      } else {
        const simpleField = searchParams.simpleField;
        params = {
          query: searchParams[simpleField]
        };
      }
  
      this.searchService.simpleSearchGroups(params).subscribe((data: any) => {
        const results = data.content || data;
        this.searchResults.emit(results);
      });
    } else {
      this.searchService.advancedSearchGroups(searchParams).subscribe((data: any) => {
        const results = data.content || data;
        this.searchResults.emit(results);
      });
    }
  }
}
