import re

file_path = r'C:\Users\olive\eclipse-workspace\myre\src\main\resources\static\js\containers\gateIn.js'
with open(file_path, 'r', encoding='utf-8') as f:
    content = f.read()

original_len = len(content)
print(f"File length: {original_len}")

# 1. Corregir submit handlers - reemplazar IDs de modal por IDs de formulario
replacements = [
    # (old, new)
    ('$("#pregateModel").submit(', '$("#pregateForm").submit('),
    ('$("#conditionModel").submit(', '$("#conditionForm").submit('),
    ('$("#newEventModal").submit(', '$("#newEventForm").submit('),
    ('$("#newContainerModal").submit(', '$("#newContainerForm").submit('),
    ('$("#addNewDamageModel").submit(', '$("#addNewDamageForm").submit('),
]

for old, new in replacements:
    count = content.count(old)
    print(f"  '{old}' found {count} times")
    content = content.replace(old, new)

# 2. Corregir containerTypeEvent - opciones del select correcto
old_container_type = 'containerTypeEvent: document.getElementById("newContainerDescription").options[$("#containerType").val()-1].text,'
new_container_type = 'containerTypeEvent: $("#unitTypeEvent option:selected").text(),'
count = content.count(old_container_type)
print(f"  containerTypeEvent found {count} times")
content = content.replace(old_container_type, new_container_type)

# 3. Corregir qualityEvent
old_quality = 'qualityEvent:  document.getElementById("qualityevent").options[$("#qualityevent").val()-1].text,'
new_quality = 'qualityEvent: $("#qualityevent option:selected").text(),'
count = content.count(old_quality)
print(f"  qualityEvent found {count} times")
content = content.replace(old_quality, new_quality)

# 4. Corregir getNomenclatura
old_nom = '\ttextContainer = document.getElementById("newContainerDescription").options[$("#containerType").val()-1].text\n\t$.ajax({\n\t\ttype: "GET",\n\t\turl: \'gateIn/getNomenclatura\','
new_nom = '\t$("#newContainerTypeSave").val($("#containerType").val());\n\ttextContainer = $("#newContainerTypeSave option:selected").text();\n\t$.ajax({\n\t\ttype: "GET",\n\t\turl: \'gateIn/getNomenclatura\','
count = content.count('\ttextContainer = document.getElementById("newContainerDescription").options[$("#containerType").val()-1].text\n\t$.ajax({\n\t\ttype: "GET",\n\t\turl: \'gateIn/getNomenclatura\',')
print(f"  getNomenclatura pattern found {count} times")
content = content.replace(
    '\ttextContainer = document.getElementById("newContainerDescription").options[$("#containerType").val()-1].text\n\t$.ajax({\n\t\ttype: "GET",\n\t\turl: \'gateIn/getNomenclatura\',',
    '\t$("#newContainerTypeSave").val($("#containerType").val());\n\ttextContainer = $("#newContainerTypeSave option:selected").text();\n\t$.ajax({\n\t\ttype: "GET",\n\t\turl: \'gateIn/getNomenclatura\','
)

# 5. Corregir showComponents
old_show = '\ttextContainer = document.getElementById("newContainerDescription").options[$("#containerType").val()-1].text\t\n\tconsole.log(textContainer)'
new_show = '\t$("#newContainerTypeSave").val($("#containerType").val());\n\ttextContainer = $("#newContainerTypeSave option:selected").text();\n\tconsole.log(textContainer)'

# Check variations
patterns_to_fix = [
    'textContainer = document.getElementById("newContainerDescription").options[$("#containerType").val()-1].text',
]
for p in patterns_to_fix:
    c = content.count(p)
    print(f"  Pattern '{p[:60]}...' found {c} times")

# Replace all remaining occurrences
old_generic = 'textContainer = document.getElementById("newContainerDescription").options[$("#containerType").val()-1].text'
new_generic = '$("#newContainerTypeSave").val($("#containerType").val());\n\ttextContainer = $("#newContainerTypeSave option:selected").text()'
count = content.count(old_generic)
print(f"  All remaining options[val-1] patterns: {count}")
content = content.replace(old_generic, new_generic)

# 6. Corregir deleteImage - eliminar fila despues de exito AJAX
old_delete = '''function deleteImage(data){
\t
\tconsole.log(data)
\t$('#imageTableInspection').DataTable().row('.selected').remove().draw(false);
\t$.ajax({
\t\ttype: "POST",
\t\turl: 'gateIn/deleteImage',
\t\tcontentType : "application/x-www-form-urlencoded; charset=UTF-8",
\t\tdata: {photoId : data},
\t\tsuccess: function(response){
\t\t\tconsole.log(response)
\t\t\talert("Se borro la imagen")
\t\t},
\t\terror: function(){
\t\t\talert("AJAX ERROR");
\t\t}
\t});
\t//$('#imageTableInspection').DataTable().row('.selected').remove().draw(false);
\t
}'''

new_delete = '''function deleteImage(data){
\t
\tconsole.log(data)
\t$.ajax({
\t\ttype: "POST",
\t\turl: 'gateIn/deleteImage',
\t\tcontentType : "application/x-www-form-urlencoded; charset=UTF-8",
\t\tdata: {photoId : data},
\t\tsuccess: function(response){
\t\t\tconsole.log(response)
\t\t\tvar table = $('#imageTableInspection').DataTable();
\t\t\ttable.rows(function(idx, rowData) {
\t\t\t\treturn rowData.photoId === data;
\t\t\t}).remove().draw(false);
\t\t\tSwal.fire("Imagen eliminada correctamente", "", "success");
\t\t},
\t\terror: function(){
\t\t\talert("AJAX ERROR");
\t\t}
\t});
\t
}'''

count = content.count('function deleteImage(data){')
print(f"  deleteImage function found {count} times")

with open(file_path, 'w', encoding='utf-8') as f:
    f.write(content)

print(f"\nFile saved. New length: {len(content)}")
print("Done!")

