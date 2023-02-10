import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { VResponseFieldComponent } from './list/v-response-field.component';
import { VResponseFieldDetailComponent } from './detail/v-response-field-detail.component';
import { VResponseFieldUpdateComponent } from './update/v-response-field-update.component';
import { VResponseFieldDeleteDialogComponent } from './delete/v-response-field-delete-dialog.component';
import { VResponseFieldRoutingModule } from './route/v-response-field-routing.module';

@NgModule({
  imports: [SharedModule, VResponseFieldRoutingModule],
  declarations: [
    VResponseFieldComponent,
    VResponseFieldDetailComponent,
    VResponseFieldUpdateComponent,
    VResponseFieldDeleteDialogComponent,
  ],
})
export class VResponseFieldModule {}
