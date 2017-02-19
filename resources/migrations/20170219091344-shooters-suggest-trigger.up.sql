--Add suggest column to shooters
ALTER TABLE shooters
  ADD COLUMN suggest TEXT;

--Function to update the shooters suggestion field
CREATE OR REPLACE FUNCTION update_shooters()
  RETURNS TRIGGER AS $$
BEGIN
  NEW.suggest := NEW.sid || ' ' || NEW.last_name || ' ' || NEW.first_name || ' ' || NEW.preferred_name || ' ' || NEW.club;
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

--Trigger to update the user suggestion field
CREATE TRIGGER update_shooters_trg
BEFORE INSERT OR UPDATE ON shooters
FOR EACH ROW EXECUTE PROCEDURE update_shooters();

--Run trigger on all existing rows
UPDATE shooters
SET first_name = first_name;