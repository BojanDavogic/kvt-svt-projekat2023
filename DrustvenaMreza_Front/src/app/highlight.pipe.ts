import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'highlight'
})
export class HighlightPipe implements PipeTransform {

  transform(text: string, searchTerm: string): string {
    if (!searchTerm || !text) return text;

    const re = new RegExp(searchTerm, 'gi');
    return text.replace(re, match => `<span class="highlight">${match}</span>`);
  }

}
