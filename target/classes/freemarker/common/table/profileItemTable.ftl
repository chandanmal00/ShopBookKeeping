

        <#if entity??>
            <table class="table table-striped" id="entityPayment">
                <thead>
                    <tr>
                        <th>Item</th>
                        <th>Item Details</th>
                    </tr>
                </thead>

                <tbody>
                    <tr>
                        <td>ItemId</td>
                        <td>${entity.get_id()}</td>
                    </tr>
                    <tr>
                        <td>${local["Barcode"]}</td>
                        <td>${entity.getBarcode()}</td>
                    </tr>
                    <td>${local["ItemType"]}</td>
                    <td>${entity.getItemType()!""}</td>
                    </tr>
                    <tr>
                    <td>${local["ProductType"]}</td>
                    <td>${entity.getProductType()!""}</td>
                    </tr>
                    <tr>  <td>${local["Brand"]}</td>
                    <td>${entity.getBrand()!""}</td>
                    </tr>
                    <tr> <td>${local["ListPrice"]}</td>
                    <td>${entity.getListPrice()!""}</td>
                    </tr>
                    <tr> <td>${local["PurchasePrice"]}</td>
                    <td>${entity.getPurchasePrice()!""}</td>
                    </tr>
                    <tr>  <td>${local["Size"]}</td>
                    <td>${entity.getSize()!""}</td>
                    </tr>
                    <tr> <td>${local["Group"]}</td>
                    <td>${entity.getGroup()!""}</td>
                    </tr>
                    <tr>  <td>${local["EventDate"]}</td>
                    <td>${entity.getEventDate()!""}</td>
                    </tr>
                    <tr>  <td>CreatedDate</td>
                    <td>${entity.getCreationDate()!""}</td>
                    </tr>
                    <tr> <td>Created By</td>
                    <td>${entity.getCreatedBy()!"root"}</td>
                    </tr>
                </tbody>
           </table>
        <#else>
           <p>
           <strong><font color="red">ERROR</font></strong>: Something went wrong, check with ADMIN or Developer
           </p>
        </#if>