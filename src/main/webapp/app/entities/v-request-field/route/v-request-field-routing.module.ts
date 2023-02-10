import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { VRequestFieldComponent } from '../list/v-request-field.component';
import { VRequestFieldDetailComponent } from '../detail/v-request-field-detail.component';
import { VRequestFieldUpdateComponent } from '../update/v-request-field-update.component';
import { VRequestFieldRoutingResolveService } from './v-request-field-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const vRequestFieldRoute: Routes = [
  {
    path: '',
    component: VRequestFieldComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: VRequestFieldDetailComponent,
    resolve: {
      vRequestField: VRequestFieldRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: VRequestFieldUpdateComponent,
    resolve: {
      vRequestField: VRequestFieldRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: VRequestFieldUpdateComponent,
    resolve: {
      vRequestField: VRequestFieldRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(vRequestFieldRoute)],
  exports: [RouterModule],
})
export class VRequestFieldRoutingModule {}
