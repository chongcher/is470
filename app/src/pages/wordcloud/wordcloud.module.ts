import { NgModule } from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { Wordcloud } from './wordcloud';

@NgModule({
  declarations: [
    Wordcloud,
  ],
  imports: [
    IonicPageModule.forChild(Wordcloud),
  ],
  exports: [
    Wordcloud
  ]
})
export class WordcloudModule {}
