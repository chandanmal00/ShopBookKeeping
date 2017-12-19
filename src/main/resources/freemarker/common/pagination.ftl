  <#function min x y>
    <#if x gt y>
        <#return y>
    <#else>
        <#return x>
    </#if>
  </#function>
  <ul class="pagination">
    <#if index gt 0>
        <li>
          <a href="/search/${searchActual}/${index-1}" aria-label="Previous">
          <span aria-hidden="true">&laquo;</span> </a>
        </li>
    </#if>
    <#if countPages-index gt 10>
        <#assign v=index+min(countPages-index,10)>
    <#else>
        <#assign v=countPages>
    </#if>
    <#list index..v as i>
        <li><a href="/search/${searchActual}/${i}">${i}</a></li>
    </#list>
    <#if index lt countPages>
        <li>
          <a href="/search/${searchActual}/${index+1}" aria-label="Next">
          <span aria-hidden="true">&raquo;</span>
        </li>
    </#if>
  </ul>