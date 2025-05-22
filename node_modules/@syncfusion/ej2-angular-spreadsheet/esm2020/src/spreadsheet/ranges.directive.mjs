import { __decorate } from "tslib";
import { Directive, ContentChildren, ContentChild } from '@angular/core';
import { ComplexBase, ArrayBase, setValue } from '@syncfusion/ej2-angular-base';
import { Template } from '@syncfusion/ej2-angular-base';
import * as i0 from "@angular/core";
let input = ['address', 'dataSource', 'fieldsOrder', 'query', 'showFieldAsHeader', 'startCell', 'template'];
let outputs = [];
/**
 * `e-range` directive represent a range of the Angular Spreadsheet.
 * It must be contained in a `e-sheet` directive.
 * ```html
 * <ejs-spreadsheet>
 *   <e-sheets>
 *    <e-sheet>
 *    <e-ranges>
 *    <e-range [dataSource]='data'></e-range>
 *    </e-ranges>
 *    </e-sheet>
 *   </e-sheets>
 * </ejs-spreadsheet>
 * ```
 */
export class RangeDirective extends ComplexBase {
    constructor(viewContainerRef) {
        super();
        this.viewContainerRef = viewContainerRef;
        setValue('currentInstance', this, this.viewContainerRef);
        this.registerEvents(outputs);
        this.directivePropList = input;
    }
}
RangeDirective.ɵfac = i0.ɵɵngDeclareFactory({ minVersion: "12.0.0", version: "13.0.3", ngImport: i0, type: RangeDirective, deps: [{ token: i0.ViewContainerRef }], target: i0.ɵɵFactoryTarget.Directive });
RangeDirective.ɵdir = i0.ɵɵngDeclareDirective({ minVersion: "12.0.0", version: "13.0.3", type: RangeDirective, selector: "e-ranges>e-range", inputs: { address: "address", dataSource: "dataSource", fieldsOrder: "fieldsOrder", query: "query", showFieldAsHeader: "showFieldAsHeader", startCell: "startCell", template: "template" }, queries: [{ propertyName: "template", first: true, predicate: ["template"], descendants: true }], usesInheritance: true, ngImport: i0 });
__decorate([
    Template()
], RangeDirective.prototype, "template", void 0);
i0.ɵɵngDeclareClassMetadata({ minVersion: "12.0.0", version: "13.0.3", ngImport: i0, type: RangeDirective, decorators: [{
            type: Directive,
            args: [{
                    selector: 'e-ranges>e-range',
                    inputs: input,
                    outputs: outputs,
                    queries: {}
                }]
        }], ctorParameters: function () { return [{ type: i0.ViewContainerRef }]; }, propDecorators: { template: [{
                type: ContentChild,
                args: ['template']
            }] } });
/**
 * Range Array Directive
 * @private
 */
export class RangesDirective extends ArrayBase {
    constructor() {
        super('ranges');
    }
}
RangesDirective.ɵfac = i0.ɵɵngDeclareFactory({ minVersion: "12.0.0", version: "13.0.3", ngImport: i0, type: RangesDirective, deps: [], target: i0.ɵɵFactoryTarget.Directive });
RangesDirective.ɵdir = i0.ɵɵngDeclareDirective({ minVersion: "12.0.0", version: "13.0.3", type: RangesDirective, selector: "e-sheet>e-ranges", queries: [{ propertyName: "children", predicate: RangeDirective }], usesInheritance: true, ngImport: i0 });
i0.ɵɵngDeclareClassMetadata({ minVersion: "12.0.0", version: "13.0.3", ngImport: i0, type: RangesDirective, decorators: [{
            type: Directive,
            args: [{
                    selector: 'e-sheet>e-ranges',
                    queries: {
                        children: new ContentChildren(RangeDirective)
                    },
                }]
        }], ctorParameters: function () { return []; } });
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoicmFuZ2VzLmRpcmVjdGl2ZS5qcyIsInNvdXJjZVJvb3QiOiIiLCJzb3VyY2VzIjpbIi4uLy4uLy4uLy4uL3NyYy9zcHJlYWRzaGVldC9yYW5nZXMuZGlyZWN0aXZlLnRzIl0sIm5hbWVzIjpbXSwibWFwcGluZ3MiOiI7QUFBQSxPQUFPLEVBQUUsU0FBUyxFQUFvQixlQUFlLEVBQUUsWUFBWSxFQUFFLE1BQU0sZUFBZSxDQUFDO0FBQzNGLE9BQU8sRUFBRSxXQUFXLEVBQUUsU0FBUyxFQUFFLFFBQVEsRUFBRSxNQUFNLDhCQUE4QixDQUFDO0FBQ2hGLE9BQU8sRUFBRSxRQUFRLEVBQUUsTUFBTSw4QkFBOEIsQ0FBQzs7QUFHeEQsSUFBSSxLQUFLLEdBQWEsQ0FBQyxTQUFTLEVBQUUsWUFBWSxFQUFFLGFBQWEsRUFBRSxPQUFPLEVBQUUsbUJBQW1CLEVBQUUsV0FBVyxFQUFFLFVBQVUsQ0FBQyxDQUFDO0FBQ3RILElBQUksT0FBTyxHQUFhLEVBQUUsQ0FBQztBQUMzQjs7Ozs7Ozs7Ozs7Ozs7R0FjRztBQVNILE1BQU0sT0FBTyxjQUFlLFNBQVEsV0FBMkI7SUErQzNELFlBQW9CLGdCQUFpQztRQUNqRCxLQUFLLEVBQUUsQ0FBQztRQURRLHFCQUFnQixHQUFoQixnQkFBZ0IsQ0FBaUI7UUFFakQsUUFBUSxDQUFDLGlCQUFpQixFQUFFLElBQUksRUFBRSxJQUFJLENBQUMsZ0JBQWdCLENBQUMsQ0FBQztRQUN6RCxJQUFJLENBQUMsY0FBYyxDQUFDLE9BQU8sQ0FBQyxDQUFDO1FBQzdCLElBQUksQ0FBQyxpQkFBaUIsR0FBRyxLQUFLLENBQUM7SUFDbkMsQ0FBQzs7MkdBcERRLGNBQWM7K0ZBQWQsY0FBYztBQTZDdkI7SUFEQyxRQUFRLEVBQUU7Z0RBQ1U7MkZBN0NaLGNBQWM7a0JBUjFCLFNBQVM7bUJBQUM7b0JBQ1AsUUFBUSxFQUFFLGtCQUFrQjtvQkFDNUIsTUFBTSxFQUFFLEtBQUs7b0JBQ2IsT0FBTyxFQUFFLE9BQU87b0JBQ2hCLE9BQU8sRUFBRSxFQUVSO2lCQUNKO3VHQThDVSxRQUFRO3NCQUZkLFlBQVk7dUJBQUMsVUFBVTs7QUFZNUI7OztHQUdHO0FBT0gsTUFBTSxPQUFPLGVBQWdCLFNBQVEsU0FBMEI7SUFDM0Q7UUFDSSxLQUFLLENBQUMsUUFBUSxDQUFDLENBQUM7SUFDcEIsQ0FBQzs7NEdBSFEsZUFBZTtnR0FBZixlQUFlLGlGQUhVLGNBQWM7MkZBR3ZDLGVBQWU7a0JBTjNCLFNBQVM7bUJBQUM7b0JBQ1AsUUFBUSxFQUFFLGtCQUFrQjtvQkFDNUIsT0FBTyxFQUFFO3dCQUNMLFFBQVEsRUFBRSxJQUFJLGVBQWUsQ0FBQyxjQUFjLENBQUM7cUJBQ2hEO2lCQUNKIiwic291cmNlc0NvbnRlbnQiOlsiaW1wb3J0IHsgRGlyZWN0aXZlLCBWaWV3Q29udGFpbmVyUmVmLCBDb250ZW50Q2hpbGRyZW4sIENvbnRlbnRDaGlsZCB9IGZyb20gJ0Bhbmd1bGFyL2NvcmUnO1xuaW1wb3J0IHsgQ29tcGxleEJhc2UsIEFycmF5QmFzZSwgc2V0VmFsdWUgfSBmcm9tICdAc3luY2Z1c2lvbi9lajItYW5ndWxhci1iYXNlJztcbmltcG9ydCB7IFRlbXBsYXRlIH0gZnJvbSAnQHN5bmNmdXNpb24vZWoyLWFuZ3VsYXItYmFzZSc7XG5cblxubGV0IGlucHV0OiBzdHJpbmdbXSA9IFsnYWRkcmVzcycsICdkYXRhU291cmNlJywgJ2ZpZWxkc09yZGVyJywgJ3F1ZXJ5JywgJ3Nob3dGaWVsZEFzSGVhZGVyJywgJ3N0YXJ0Q2VsbCcsICd0ZW1wbGF0ZSddO1xubGV0IG91dHB1dHM6IHN0cmluZ1tdID0gW107XG4vKipcbiAqIGBlLXJhbmdlYCBkaXJlY3RpdmUgcmVwcmVzZW50IGEgcmFuZ2Ugb2YgdGhlIEFuZ3VsYXIgU3ByZWFkc2hlZXQuXG4gKiBJdCBtdXN0IGJlIGNvbnRhaW5lZCBpbiBhIGBlLXNoZWV0YCBkaXJlY3RpdmUuXG4gKiBgYGBodG1sXG4gKiA8ZWpzLXNwcmVhZHNoZWV0PlxuICogICA8ZS1zaGVldHM+XG4gKiAgICA8ZS1zaGVldD5cbiAqICAgIDxlLXJhbmdlcz5cbiAqICAgIDxlLXJhbmdlIFtkYXRhU291cmNlXT0nZGF0YSc+PC9lLXJhbmdlPlxuICogICAgPC9lLXJhbmdlcz5cbiAqICAgIDwvZS1zaGVldD5cbiAqICAgPC9lLXNoZWV0cz5cbiAqIDwvZWpzLXNwcmVhZHNoZWV0PlxuICogYGBgXG4gKi9cbkBEaXJlY3RpdmUoe1xuICAgIHNlbGVjdG9yOiAnZS1yYW5nZXM+ZS1yYW5nZScsXG4gICAgaW5wdXRzOiBpbnB1dCxcbiAgICBvdXRwdXRzOiBvdXRwdXRzLCAgICBcbiAgICBxdWVyaWVzOiB7XG5cbiAgICB9XG59KVxuZXhwb3J0IGNsYXNzIFJhbmdlRGlyZWN0aXZlIGV4dGVuZHMgQ29tcGxleEJhc2U8UmFuZ2VEaXJlY3RpdmU+IHtcbiAgICBwdWJsaWMgZGlyZWN0aXZlUHJvcExpc3Q6IGFueTtcblx0XG5cblxuICAgIC8qKiBcbiAgICAgKiBTcGVjaWZpZXMgdGhlIGFkZHJlc3MgZm9yIHVwZGF0aW5nIHRoZSBkYXRhU291cmNlIG9yIHRlbXBsYXRlLlxuICAgICAqIEBkZWZhdWx0ICdBMSdcbiAgICAgKi9cbiAgICBwdWJsaWMgYWRkcmVzczogYW55O1xuICAgIC8qKiBcbiAgICAgKiBTcGVjaWZpZXMgdGhlIGRhdGEgYXMgSlNPTiAvIERhdGEgbWFuYWdlciB0byB0aGUgc2hlZXQuXG4gICAgICogQGRlZmF1bHQgbnVsbFxuICAgICAqL1xuICAgIHB1YmxpYyBkYXRhU291cmNlOiBhbnk7XG4gICAgLyoqIFxuICAgICAqIEJ5IGRlZmF1bHQsIHdoZW4gYSBzaGVldCBpcyBib3VuZCB0byBhIGRhdGEgc291cmNlLCBjb2x1bW5zIGFyZSBhc3NpZ25lZCB0byBkYXRhIHNvdXJjZSBmaWVsZHMgc2VxdWVudGlhbGx5LiBcbiAgICAgKiBUaGlzIG1lYW5zIHRoYXQgdGhlIGZpcnN0IGRhdGEgZmllbGQgaXMgYXNzaWduZWQgdG8gQ29sdW1uIEEsIHRoZSBzZWNvbmQgdG8gQ29sdW1uIEIsIGFuZCBzbyBvbi4gXG4gICAgICogWW91IGNhbiBjdXN0b21pemUgdGhlc2UgYXNzaWdubWVudHMgYnkgc3BlY2lmeWluZyB0aGUgZmllbGQgbmFtZXMgaW4gdGhlIGRlc2lyZWQgY29sdW1uIG9yZGVyIHVzaW5nIHRoZSAnZmllbGRzT3JkZXInIHByb3BlcnR5LlxuICAgICAqIEBkZWZhdWx0IG51bGxcbiAgICAgKi9cbiAgICBwdWJsaWMgZmllbGRzT3JkZXI6IGFueTtcbiAgICAvKiogXG4gICAgICogRGVmaW5lcyB0aGUgZXh0ZXJuYWwgW2BRdWVyeWBdKGh0dHBzOi8vZWoyLnN5bmNmdXNpb24uY29tL2RvY3VtZW50YXRpb24vZGF0YS9hcGktcXVlcnkuaHRtbCkgXG4gICAgICogdGhhdCB3aWxsIGJlIGV4ZWN1dGVkIGFsb25nIHdpdGggZGF0YSBwcm9jZXNzaW5nLlxuICAgICAqIEBkZWZhdWx0IG51bGxcbiAgICAgKi9cbiAgICBwdWJsaWMgcXVlcnk6IGFueTtcbiAgICAvKiogXG4gICAgICogU2hvdy9IaWRlIHRoZSBmaWVsZCBvZiB0aGUgZGF0YXNvdXJjZSBhcyBoZWFkZXIuXG4gICAgICogQGRlZmF1bHQgdHJ1ZVxuICAgICAqL1xuICAgIHB1YmxpYyBzaG93RmllbGRBc0hlYWRlcjogYW55O1xuICAgIC8qKiBcbiAgICAgKiBTcGVjaWZpZXMgdGhlIHN0YXJ0IGNlbGwgZnJvbSB3aGljaCB0aGUgZGF0YXNvdXJjZSB3aWxsIGJlIHBvcHVsYXRlZC5cbiAgICAgKiBAZGVmYXVsdCAnQTEnXG4gICAgICovXG4gICAgcHVibGljIHN0YXJ0Q2VsbDogYW55O1xuICAgIC8qKiBcbiAgICAgKiBUZW1wbGF0ZSBoZWxwcyB0byBjb21waWxlcyB0aGUgZ2l2ZW4gSFRNTCBTdHJpbmcgKG9yIEhUTUwgRWxlbWVudCBJRCkgaW50byBIdE1MIEVsZW1lbnQgYW5kIGFwcGVuZCB0byB0aGUgQ2VsbC5cbiAgICAgKiBAZGVmYXVsdCAnJ1xuICAgICAqIEBhc3B0eXBlIHN0cmluZ1xuICAgICAqL1xuICAgIEBDb250ZW50Q2hpbGQoJ3RlbXBsYXRlJylcbiAgICBAVGVtcGxhdGUoKVxuICAgIHB1YmxpYyB0ZW1wbGF0ZTogYW55O1xuXG4gICAgY29uc3RydWN0b3IocHJpdmF0ZSB2aWV3Q29udGFpbmVyUmVmOlZpZXdDb250YWluZXJSZWYpIHtcbiAgICAgICAgc3VwZXIoKTtcbiAgICAgICAgc2V0VmFsdWUoJ2N1cnJlbnRJbnN0YW5jZScsIHRoaXMsIHRoaXMudmlld0NvbnRhaW5lclJlZik7XG4gICAgICAgIHRoaXMucmVnaXN0ZXJFdmVudHMob3V0cHV0cyk7XG4gICAgICAgIHRoaXMuZGlyZWN0aXZlUHJvcExpc3QgPSBpbnB1dDtcbiAgICB9XG59XG5cbi8qKlxuICogUmFuZ2UgQXJyYXkgRGlyZWN0aXZlXG4gKiBAcHJpdmF0ZVxuICovXG5ARGlyZWN0aXZlKHtcbiAgICBzZWxlY3RvcjogJ2Utc2hlZXQ+ZS1yYW5nZXMnLFxuICAgIHF1ZXJpZXM6IHtcbiAgICAgICAgY2hpbGRyZW46IG5ldyBDb250ZW50Q2hpbGRyZW4oUmFuZ2VEaXJlY3RpdmUpXG4gICAgfSxcbn0pXG5leHBvcnQgY2xhc3MgUmFuZ2VzRGlyZWN0aXZlIGV4dGVuZHMgQXJyYXlCYXNlPFJhbmdlc0RpcmVjdGl2ZT4ge1xuICAgIGNvbnN0cnVjdG9yKCkge1xuICAgICAgICBzdXBlcigncmFuZ2VzJyk7XG4gICAgfVxufSJdfQ==