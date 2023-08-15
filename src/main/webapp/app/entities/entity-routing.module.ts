import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'employee',
        data: { pageTitle: 'jhipsterSampleApplicationApp.employee.home.title' },
        loadChildren: () => import('./employee/employee.routes'),
      },
      {
        path: 'location',
        data: { pageTitle: 'jhipsterSampleApplicationApp.location.home.title' },
        loadChildren: () => import('./location/location.routes'),
      },
      {
        path: 'task',
        data: { pageTitle: 'jhipsterSampleApplicationApp.task.home.title' },
        loadChildren: () => import('./task/task.routes'),
      },
      {
        path: 'department',
        data: { pageTitle: 'jhipsterSampleApplicationApp.department.home.title' },
        loadChildren: () => import('./department/department.routes'),
      },
      {
        path: 'job',
        data: { pageTitle: 'jhipsterSampleApplicationApp.job.home.title' },
        loadChildren: () => import('./job/job.routes'),
      },
      {
        path: 'region',
        data: { pageTitle: 'jhipsterSampleApplicationApp.region.home.title' },
        loadChildren: () => import('./region/region.routes'),
      },
      {
        path: 'job-history',
        data: { pageTitle: 'jhipsterSampleApplicationApp.jobHistory.home.title' },
        loadChildren: () => import('./job-history/job-history.routes'),
      },
      {
        path: 'country',
        data: { pageTitle: 'jhipsterSampleApplicationApp.country.home.title' },
        loadChildren: () => import('./country/country.routes'),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
