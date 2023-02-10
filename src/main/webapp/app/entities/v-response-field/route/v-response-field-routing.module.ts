import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { VResponseFieldComponent } from '../list/v-response-field.component';
import { VResponseFieldDetailComponent } from '../detail/v-response-field-detail.component';
import { VResponseFieldUpdateComponent } from '../update/v-response-field-update.component';
import { VResponseFieldRoutingResolveService } from './v-response-field-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const vResponseFieldRoute: Routes = [
  {
    path: '',
    component: VResponseFieldComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: VResponseFieldDetailComponent,
    resolve: {
      vResponseField: VResponseFieldRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: VResponseFieldUpdateComponent,
    resolve: {
      vResponseField: VResponseFieldRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: VResponseFieldUpdateComponent,
    resolve: {
      vResponseField: VResponseFieldRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(vResponseFieldRoute)],
  exports: [RouterModule],
})
export class VResponseFieldRoutingModule {}
