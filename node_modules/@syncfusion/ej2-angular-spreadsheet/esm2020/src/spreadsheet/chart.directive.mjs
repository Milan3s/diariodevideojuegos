import { Directive, ContentChildren } from '@angular/core';
import { ComplexBase, ArrayBase, setValue } from '@syncfusion/ej2-angular-base';
import * as i0 from "@angular/core";
let input = ['dataLabelSettings', 'height', 'id', 'isSeriesInRows', 'legendSettings', 'markerSettings', 'primaryXAxis', 'primaryYAxis', 'range', 'theme', 'title', 'type', 'width'];
let outputs = [];
export class ChartDirective extends ComplexBase {
    constructor(viewContainerRef) {
        super();
        this.viewContainerRef = viewContainerRef;
        setValue('currentInstance', this, this.viewContainerRef);
        this.registerEvents(outputs);
        this.directivePropList = input;
    }
}
ChartDirective.ɵfac = i0.ɵɵngDeclareFactory({ minVersion: "12.0.0", version: "13.0.3", ngImport: i0, type: ChartDirective, deps: [{ token: i0.ViewContainerRef }], target: i0.ɵɵFactoryTarget.Directive });
ChartDirective.ɵdir = i0.ɵɵngDeclareDirective({ minVersion: "12.0.0", version: "13.0.3", type: ChartDirective, selector: "e-charts>e-chart", inputs: { dataLabelSettings: "dataLabelSettings", height: "height", id: "id", isSeriesInRows: "isSeriesInRows", legendSettings: "legendSettings", markerSettings: "markerSettings", primaryXAxis: "primaryXAxis", primaryYAxis: "primaryYAxis", range: "range", theme: "theme", title: "title", type: "type", width: "width" }, usesInheritance: true, ngImport: i0 });
i0.ɵɵngDeclareClassMetadata({ minVersion: "12.0.0", version: "13.0.3", ngImport: i0, type: ChartDirective, decorators: [{
            type: Directive,
            args: [{
                    selector: 'e-charts>e-chart',
                    inputs: input,
                    outputs: outputs,
                    queries: {}
                }]
        }], ctorParameters: function () { return [{ type: i0.ViewContainerRef }]; } });
/**
 * Chart Array Directive
 * @private
 */
export class ChartsDirective extends ArrayBase {
    constructor() {
        super('chart');
    }
}
ChartsDirective.ɵfac = i0.ɵɵngDeclareFactory({ minVersion: "12.0.0", version: "13.0.3", ngImport: i0, type: ChartsDirective, deps: [], target: i0.ɵɵFactoryTarget.Directive });
ChartsDirective.ɵdir = i0.ɵɵngDeclareDirective({ minVersion: "12.0.0", version: "13.0.3", type: ChartsDirective, selector: "e-cell>e-charts", queries: [{ propertyName: "children", predicate: ChartDirective }], usesInheritance: true, ngImport: i0 });
i0.ɵɵngDeclareClassMetadata({ minVersion: "12.0.0", version: "13.0.3", ngImport: i0, type: ChartsDirective, decorators: [{
            type: Directive,
            args: [{
                    selector: 'e-cell>e-charts',
                    queries: {
                        children: new ContentChildren(ChartDirective)
                    },
                }]
        }], ctorParameters: function () { return []; } });
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiY2hhcnQuZGlyZWN0aXZlLmpzIiwic291cmNlUm9vdCI6IiIsInNvdXJjZXMiOlsiLi4vLi4vLi4vLi4vc3JjL3NwcmVhZHNoZWV0L2NoYXJ0LmRpcmVjdGl2ZS50cyJdLCJuYW1lcyI6W10sIm1hcHBpbmdzIjoiQUFBQSxPQUFPLEVBQUUsU0FBUyxFQUFvQixlQUFlLEVBQUUsTUFBTSxlQUFlLENBQUM7QUFDN0UsT0FBTyxFQUFFLFdBQVcsRUFBRSxTQUFTLEVBQUUsUUFBUSxFQUFFLE1BQU0sOEJBQThCLENBQUM7O0FBSWhGLElBQUksS0FBSyxHQUFhLENBQUMsbUJBQW1CLEVBQUUsUUFBUSxFQUFFLElBQUksRUFBRSxnQkFBZ0IsRUFBRSxnQkFBZ0IsRUFBRSxnQkFBZ0IsRUFBRSxjQUFjLEVBQUUsY0FBYyxFQUFFLE9BQU8sRUFBRSxPQUFPLEVBQUUsT0FBTyxFQUFFLE1BQU0sRUFBRSxPQUFPLENBQUMsQ0FBQztBQUM5TCxJQUFJLE9BQU8sR0FBYSxFQUFFLENBQUM7QUFVM0IsTUFBTSxPQUFPLGNBQWUsU0FBUSxXQUEyQjtJQXVFM0QsWUFBb0IsZ0JBQWlDO1FBQ2pELEtBQUssRUFBRSxDQUFDO1FBRFEscUJBQWdCLEdBQWhCLGdCQUFnQixDQUFpQjtRQUVqRCxRQUFRLENBQUMsaUJBQWlCLEVBQUUsSUFBSSxFQUFFLElBQUksQ0FBQyxnQkFBZ0IsQ0FBQyxDQUFDO1FBQ3pELElBQUksQ0FBQyxjQUFjLENBQUMsT0FBTyxDQUFDLENBQUM7UUFDN0IsSUFBSSxDQUFDLGlCQUFpQixHQUFHLEtBQUssQ0FBQztJQUNuQyxDQUFDOzsyR0E1RVEsY0FBYzsrRkFBZCxjQUFjOzJGQUFkLGNBQWM7a0JBUjFCLFNBQVM7bUJBQUM7b0JBQ1AsUUFBUSxFQUFFLGtCQUFrQjtvQkFDNUIsTUFBTSxFQUFFLEtBQUs7b0JBQ2IsT0FBTyxFQUFFLE9BQU87b0JBQ2hCLE9BQU8sRUFBRSxFQUVSO2lCQUNKOztBQWdGRDs7O0dBR0c7QUFPSCxNQUFNLE9BQU8sZUFBZ0IsU0FBUSxTQUEwQjtJQUMzRDtRQUNJLEtBQUssQ0FBQyxPQUFPLENBQUMsQ0FBQztJQUNuQixDQUFDOzs0R0FIUSxlQUFlO2dHQUFmLGVBQWUsZ0ZBSFUsY0FBYzsyRkFHdkMsZUFBZTtrQkFOM0IsU0FBUzttQkFBQztvQkFDUCxRQUFRLEVBQUUsaUJBQWlCO29CQUMzQixPQUFPLEVBQUU7d0JBQ0wsUUFBUSxFQUFFLElBQUksZUFBZSxDQUFDLGNBQWMsQ0FBQztxQkFDaEQ7aUJBQ0oiLCJzb3VyY2VzQ29udGVudCI6WyJpbXBvcnQgeyBEaXJlY3RpdmUsIFZpZXdDb250YWluZXJSZWYsIENvbnRlbnRDaGlsZHJlbiB9IGZyb20gJ0Bhbmd1bGFyL2NvcmUnO1xuaW1wb3J0IHsgQ29tcGxleEJhc2UsIEFycmF5QmFzZSwgc2V0VmFsdWUgfSBmcm9tICdAc3luY2Z1c2lvbi9lajItYW5ndWxhci1iYXNlJztcblxuXG5cbmxldCBpbnB1dDogc3RyaW5nW10gPSBbJ2RhdGFMYWJlbFNldHRpbmdzJywgJ2hlaWdodCcsICdpZCcsICdpc1Nlcmllc0luUm93cycsICdsZWdlbmRTZXR0aW5ncycsICdtYXJrZXJTZXR0aW5ncycsICdwcmltYXJ5WEF4aXMnLCAncHJpbWFyeVlBeGlzJywgJ3JhbmdlJywgJ3RoZW1lJywgJ3RpdGxlJywgJ3R5cGUnLCAnd2lkdGgnXTtcbmxldCBvdXRwdXRzOiBzdHJpbmdbXSA9IFtdO1xuXG5ARGlyZWN0aXZlKHtcbiAgICBzZWxlY3RvcjogJ2UtY2hhcnRzPmUtY2hhcnQnLFxuICAgIGlucHV0czogaW5wdXQsXG4gICAgb3V0cHV0czogb3V0cHV0cywgICAgXG4gICAgcXVlcmllczoge1xuXG4gICAgfVxufSlcbmV4cG9ydCBjbGFzcyBDaGFydERpcmVjdGl2ZSBleHRlbmRzIENvbXBsZXhCYXNlPENoYXJ0RGlyZWN0aXZlPiB7XG4gICAgcHVibGljIGRpcmVjdGl2ZVByb3BMaXN0OiBhbnk7XG5cdFxuXG5cbiAgICAvKiogXG4gICAgICogU3BlY2lmaWVzIHRoZSB0eXBlIG9mIGEgY2hhcnQuXG4gICAgICogQGRlZmF1bHQgJ0xpbmUnXG4gICAgICovXG4gICAgcHVibGljIHR5cGU6IGFueTtcbiAgICAvKiogXG4gICAgICogVGhlIGRhdGEgbGFiZWwgZm9yIHRoZSBzZXJpZXMuXG4gICAgICogQGRlZmF1bHQge31cbiAgICAgKi9cbiAgICBwdWJsaWMgZGF0YUxhYmVsU2V0dGluZ3M6IGFueTtcbiAgICAvKiogXG4gICAgICogU3BlY2lmaWVzIHRoZSBoZWlnaHQgb2YgdGhlIGNoYXJ0LlxuICAgICAqIEBkZWZhdWx0IDI5MFxuICAgICAqL1xuICAgIHB1YmxpYyBoZWlnaHQ6IGFueTtcbiAgICAvKiogXG4gICAgICogU3BlY2lmaWVzIGNoYXJ0IGVsZW1lbnQgaWQuXG4gICAgICogQGRlZmF1bHQgJydcbiAgICAgKi9cbiAgICBwdWJsaWMgaWQ6IGFueTtcbiAgICAvKiogXG4gICAgICogU3BlY2lmaWVzIHRvIHN3aXRjaCB0aGUgcm93IG9yIGEgY29sdW1uLlxuICAgICAqIEBkZWZhdWx0IGZhbHNlXG4gICAgICovXG4gICAgcHVibGljIGlzU2VyaWVzSW5Sb3dzOiBhbnk7XG4gICAgLyoqIFxuICAgICAqIE9wdGlvbnMgZm9yIGN1c3RvbWl6aW5nIHRoZSBsZWdlbmQgb2YgdGhlIGNoYXJ0LlxuICAgICAqIEBkZWZhdWx0IHt9XG4gICAgICovXG4gICAgcHVibGljIGxlZ2VuZFNldHRpbmdzOiBhbnk7XG4gICAgLyoqIFxuICAgICAqIE9wdGlvbnMgdG8gY29uZmlndXJlIHRoZSBtYXJrZXJcbiAgICAgKiBAZGVmYXVsdCB7fVxuICAgICAqL1xuICAgIHB1YmxpYyBtYXJrZXJTZXR0aW5nczogYW55O1xuICAgIC8qKiBcbiAgICAgKiBPcHRpb25zIHRvIGNvbmZpZ3VyZSB0aGUgaG9yaXpvbnRhbCBheGlzLlxuICAgICAqIEBkZWZhdWx0IHt9XG4gICAgICovXG4gICAgcHVibGljIHByaW1hcnlYQXhpczogYW55O1xuICAgIC8qKiBcbiAgICAgKiBPcHRpb25zIHRvIGNvbmZpZ3VyZSB0aGUgdmVydGljYWwgYXhpcy5cbiAgICAgKiBAZGVmYXVsdCB7fVxuICAgICAqL1xuICAgIHB1YmxpYyBwcmltYXJ5WUF4aXM6IGFueTtcbiAgICAvKiogXG4gICAgICogU3BlY2lmaWVzIHRoZSBzZWxlY3RlZCByYW5nZSBvciBzcGVjaWZpZWQgcmFuZ2UuXG4gICAgICogQGRlZmF1bHQgJydcbiAgICAgKi9cbiAgICBwdWJsaWMgcmFuZ2U6IGFueTtcbiAgICAvKiogXG4gICAgICogU3BlY2lmaWVzIHRoZSB0aGVtZSBvZiBhIGNoYXJ0LlxuICAgICAqIEBkZWZhdWx0ICdNYXRlcmlhbCdcbiAgICAgKi9cbiAgICBwdWJsaWMgdGhlbWU6IGFueTtcbiAgICAvKiogXG4gICAgICogVGl0bGUgb2YgdGhlIGNoYXJ0XG4gICAgICogQGRlZmF1bHQgJydcbiAgICAgKi9cbiAgICBwdWJsaWMgdGl0bGU6IGFueTtcbiAgICAvKiogXG4gICAgICogU3BlY2lmaWVzIHRoZSB3aWR0aCBvZiB0aGUgY2hhcnQuXG4gICAgICogQGRlZmF1bHQgNDgwXG4gICAgICovXG4gICAgcHVibGljIHdpZHRoOiBhbnk7XG5cbiAgICBjb25zdHJ1Y3Rvcihwcml2YXRlIHZpZXdDb250YWluZXJSZWY6Vmlld0NvbnRhaW5lclJlZikge1xuICAgICAgICBzdXBlcigpO1xuICAgICAgICBzZXRWYWx1ZSgnY3VycmVudEluc3RhbmNlJywgdGhpcywgdGhpcy52aWV3Q29udGFpbmVyUmVmKTtcbiAgICAgICAgdGhpcy5yZWdpc3RlckV2ZW50cyhvdXRwdXRzKTtcbiAgICAgICAgdGhpcy5kaXJlY3RpdmVQcm9wTGlzdCA9IGlucHV0O1xuICAgIH1cbn1cblxuLyoqXG4gKiBDaGFydCBBcnJheSBEaXJlY3RpdmVcbiAqIEBwcml2YXRlXG4gKi9cbkBEaXJlY3RpdmUoe1xuICAgIHNlbGVjdG9yOiAnZS1jZWxsPmUtY2hhcnRzJyxcbiAgICBxdWVyaWVzOiB7XG4gICAgICAgIGNoaWxkcmVuOiBuZXcgQ29udGVudENoaWxkcmVuKENoYXJ0RGlyZWN0aXZlKVxuICAgIH0sXG59KVxuZXhwb3J0IGNsYXNzIENoYXJ0c0RpcmVjdGl2ZSBleHRlbmRzIEFycmF5QmFzZTxDaGFydHNEaXJlY3RpdmU+IHtcbiAgICBjb25zdHJ1Y3RvcigpIHtcbiAgICAgICAgc3VwZXIoJ2NoYXJ0Jyk7XG4gICAgfVxufSJdfQ==