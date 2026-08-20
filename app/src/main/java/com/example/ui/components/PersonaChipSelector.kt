package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Persona
import com.example.model.PersonaRepository
import com.example.ui.theme.AdemBorderGlow
import com.example.ui.theme.AdemBorderSubtle
import com.example.ui.theme.AdemCyanPrimary
import com.example.ui.theme.AdemDarkSurface
import com.example.ui.theme.AdemDarkSurfaceVariant

@Composable
fun PersonaChipSelector(
    selectedPersonaId: String,
    onPersonaSelected: (Persona) -> Unit,
    modifier: Modifier = Modifier
) {
    val personas = PersonaRepository.personas
    val scrollState = rememberScrollState()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        personas.forEach { persona ->
            val isSelected = persona.id == selectedPersonaId
            PersonaChip(
                persona = persona,
                isSelected = isSelected,
                onClick = { onPersonaSelected(persona) }
            )
        }
    }
}

@Composable
fun PersonaChip(
    persona: Persona,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) Color(0xFF0C2738) else AdemDarkSurfaceVariant
    val borderColor = if (isSelected) AdemCyanPrimary else AdemBorderSubtle
    val iconColor = if (isSelected) AdemCyanPrimary else Color(0xFF9CA3AF)
    val textColor = if (isSelected) AdemCyanPrimary else MaterialTheme.colorScheme.onSurfaceVariant

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(backgroundColor)
            .border(
                width = if (isSelected) 1.5.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(20.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 7.dp)
            .testTag("persona_chip_${persona.id}")
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = persona.icon,
                contentDescription = persona.nameAr,
                tint = iconColor,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = persona.nameAr,
                color = textColor,
                fontSize = 13.sp,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
            )
        }
    }
}
