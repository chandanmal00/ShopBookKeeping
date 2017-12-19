

       <#if entity.loyalty??>
              <tr>
                   <td>${local["LoyaltyType"]}</td>
                   <td>${entity.getLoyalty().getType()!""}</td>

              </tr>
              <tr>
                   <td>${local["LoyaltyNumber"]}</td>
                   <td>${entity.getLoyalty().getNumber()!""}</td>

              </tr>
       </#if>

